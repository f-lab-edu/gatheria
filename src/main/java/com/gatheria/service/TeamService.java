package com.gatheria.service;

import com.gatheria.domain.Lecture;
import com.gatheria.domain.Member;
import com.gatheria.domain.Team;
import com.gatheria.domain.TeamMember;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MemberRole;
import com.gatheria.dto.response.StudentResponseDto;
import com.gatheria.dto.response.TeamResponseDto;
import com.gatheria.mapper.LectureMapper;
import com.gatheria.mapper.MemberMapper;
import com.gatheria.mapper.TeamMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

  private final TeamMapper teamMapper;
  private final LectureMapper lectureMapper;
  private final MemberMapper memberMapper;


  public TeamService(TeamMapper teamMapper, LectureMapper lectureMapper,
      MemberMapper memberMapper) {
    this.teamMapper = teamMapper;
    this.lectureMapper = lectureMapper;
    this.memberMapper = memberMapper;
  }

  @Transactional
  public void autoAssignTeams(Long lectureId, int teamSize, AuthInfo authInfo) {

    validateInstructorAuth(authInfo);

    validateLectureAccess(lectureId, authInfo);

    List<Team> existingTeams = teamMapper.findTeamsByLectureId(lectureId);

    if (!existingTeams.isEmpty()) {
      throw new IllegalStateException("");
    }

    List<Long> studentIds = lectureMapper.findStudentIdsByLectureId(lectureId);
    if (studentIds.isEmpty()) {
      throw new IllegalArgumentException("");
    }

    Collections.shuffle(studentIds);

    int totalStudents = studentIds.size();
    int teamCount = totalStudents / teamSize;
    List<Team> newTeams = new ArrayList<>();

    for (int i = 0; i < teamCount; i++) {
      String teamName = String.valueOf('A' + i);
      Team newTeam = Team.of(teamName, lectureId);
      newTeams.add(newTeam);
    }

    teamMapper.saveTeams(newTeams);

    List<TeamMember> teamMembers = new ArrayList<>();
    int extraCount = totalStudents % teamCount;

    int studentIdx = 0;
    for (int i = 0; i < teamCount; i++) {
      int memberCount;
      if (i < extraCount) {
        memberCount = teamSize + 1;
      } else {
        memberCount = teamSize;
      }

      for (int j = 0; j < memberCount; j++) {
        Long studentId = studentIds.get(studentIdx++);
        Long teamID = newTeams.get(i).getId();
        TeamMember teamMember = TeamMember.of(teamID, studentId, lectureId);
        teamMembers.add(teamMember);
      }

    }

    teamMapper.saveTeamMembers(teamMembers);
  }

  public void manualAssignTeams(Long lectureId, Long teamId, Long studentId, AuthInfo authInfo) {
    //TODO : 학생이 그 수업에 있는지 검증하는 로직이 빠짐

    validateInstructorAuth(authInfo);
    validateLectureAccess(lectureId, authInfo);

    List<Team> existingTeams = teamMapper.findTeamsByLectureId(lectureId);

    if (existingTeams.isEmpty()) {
      throw new IllegalStateException("자동배정 필요");
    }

    Team targetTeam = validateTeam(teamId, lectureId);

    TeamMember existingMember = teamMapper.findTeamMemberByStudentIdAndLectureId(studentId,
        targetTeam.getLectureId());

    if (existingMember == null) {
      TeamMember newMember = TeamMember.of(targetTeam.getId(), studentId,
          targetTeam.getLectureId());
      teamMapper.saveTeamMembers(Collections.singletonList(newMember));
    } else {
      existingMember.updateTeamId(targetTeam.getId());
      teamMapper.updateTeamMember(existingMember);
    }
  }

  public List<TeamResponseDto> getTeamsByLectureId(Long lectureId, AuthInfo authInfo) {
    validateLectureAccess(lectureId, authInfo);

    List<Team> teams = teamMapper.findTeamsByLectureId(lectureId);

    List<TeamMember> teamMembers = teamMapper.findTeamMembersByLectureId(lectureId);

    List<Long> studentIds = teamMembers.stream().map(TeamMember::getStudentId).toList();
    List<Member> students = memberMapper.findStudentsByIds(studentIds);

    Map<Long, Member> studentMap = students.stream()
        .collect(Collectors.toMap(Member::getId, m -> m));

    Map<Long, List<StudentResponseDto>> studentByTeam = teamMembers.stream()
        .collect(Collectors.groupingBy(
            TeamMember::getTeamId, Collectors.mapping(
                member -> StudentResponseDto.of(studentMap.get(member.getStudentId())),
                Collectors.toList()
            )
        ));

    return teams.stream()
        .map(team -> TeamResponseDto.of(
            team.getId(),
            team.getName(),
            lectureId,
            studentByTeam.getOrDefault(team.getId(), Collections.emptyList())
        )).toList();
  }

  public TeamResponseDto getTeamByTeamId(Long lectureId, Long teamId, AuthInfo authInfo) {
    validateLectureAccess(lectureId, authInfo);

    Team team = teamMapper.findTeamByTeamId(teamId);
    if (team == null || !team.getLectureId().equals(lectureId)) {
      throw new IllegalArgumentException("해당 강의에 없는 팀");
    }

    List<TeamMember> teamMembers = teamMapper.findTeamMembersByTeamId(teamId);

    if (teamMembers.isEmpty()) {
      return TeamResponseDto.of(team.getId(), team.getName(), lectureId, Collections.emptyList());
    }

    List<Long> studentIds = teamMembers.stream().map(TeamMember::getStudentId).toList();
    List<Member> students = memberMapper.findStudentsByIds(studentIds);

    Map<Long, Member> studentMap = students.stream()
        .collect(Collectors.toMap(Member::getId, m -> m));

    List<StudentResponseDto> studentDtos = teamMembers.stream()
        .map(member -> StudentResponseDto.of(studentMap.get(member.getStudentId())))
        .toList();

    return TeamResponseDto.of(team.getId(), team.getName(), lectureId, studentDtos);
  }


  private void validateInstructorAuth(AuthInfo authInfo) {
    if (authInfo.getRole() != MemberRole.INSTRUCTOR) {
      throw new RuntimeException();
    }
  }

  private void validateLectureAccess(long lectureId, AuthInfo authInfo) {
    Lecture lecture = lectureMapper.findLectureById(lectureId);

    if (lecture == null) {
      throw new IllegalArgumentException("Invalid Lecture");
    }

    if (authInfo.getRole() == MemberRole.INSTRUCTOR) {
      if (!Objects.equals(lecture.getInstructorId(), authInfo.getMemberId())) {
        throw new IllegalArgumentException("교수자는 해당 수업에 접근 권한 x");
      }
    } else if (authInfo.getRole() == MemberRole.STUDENT) {
      boolean isEnrolled = lectureMapper.existEnrollmentByStudentIdAndLectureID(
          authInfo.getMemberId(), lectureId);
      if (!isEnrolled) {
        throw new IllegalArgumentException("학생이 해당 수업에 등록 x");
      }

    } else {
      throw new IllegalArgumentException("Invalid Role");
    }
  }

  private Team validateTeam(Long teamId, Long lectureId) {
    Team targetTeam = teamMapper.findTeamByTeamId(teamId);
    if (targetTeam == null || !Objects.equals(targetTeam.getLectureId(), lectureId)) {
      throw new IllegalArgumentException("유효하지 않은 팀.");
    }
    return targetTeam;
  }


}
