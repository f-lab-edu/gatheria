package com.gatheria.service;

import com.gatheria.domain.Lecture;
import com.gatheria.domain.Team;
import com.gatheria.domain.TeamMember;
import com.gatheria.domain.TeamMembers;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.response.StudentInfoResponseDto;
import com.gatheria.dto.response.TeamResponseDto;
import com.gatheria.mapper.LectureMapper;
import com.gatheria.mapper.MemberMapper;
import com.gatheria.mapper.TeamMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

    validateLectureAccessForInstructor(lectureId, authInfo);

    List<Team> existingTeams = teamMapper.findTeamsByLectureId(lectureId);
    if (!existingTeams.isEmpty()) {
      throw new IllegalStateException("이미 팀이 존재하는 수업입니다");
    }

    List<Long> studentIds = getEnrolledStudentIds(lectureId);
    if (studentIds.isEmpty()) {
      throw new IllegalArgumentException("등록된 학생이 없다.");
    }

    Collections.shuffle(studentIds);

    int totalStudents = studentIds.size();
    int teamCount = totalStudents / teamSize;

    List<Team> newTeams = Team.createTeams(lectureId, teamCount);
    teamMapper.saveTeams(newTeams);

    TeamMembers teamMembers = TeamMembers.create(newTeams, studentIds, lectureId, teamSize);
    teamMapper.saveTeamMembers(teamMembers.getMembers());
  }


  public void manualAssignTeams(Long lectureId, Long teamId, Long studentId, AuthInfo authInfo) {

    validateLectureAccessForInstructor(lectureId, authInfo);

    boolean isStudentEnrolled = lectureMapper.existEnrollmentByStudentIdAndLectureID(
        studentId, lectureId);
    if (!isStudentEnrolled) {
      throw new IllegalArgumentException("수업에 등록되지 않은 학생");
    }

    List<Team> existingTeams = teamMapper.findTeamsByLectureId(lectureId);
    if (existingTeams.isEmpty()) {
      throw new IllegalStateException("자동배정 필요");
    }

    validateTeam(teamId, lectureId);

    TeamMember teamMember = TeamMember.of(teamId, studentId, lectureId);
    teamMapper.upsertTeamMember(teamMember);

  }

  public List<TeamResponseDto> getTeamsWithStudentsByLecture(Long lectureId, AuthInfo authInfo) {
    validateLectureAccessForInstructor(lectureId, authInfo);

    List<Team> teams = teamMapper.findTeamsByLectureId(lectureId);

    List<Long> studentIds = lectureMapper.findStudentIdsByLectureId(lectureId);

    Map<Long, StudentInfoResponseDto> studentDtoMap = memberMapper.findStudentInfoMapByIds(
        studentIds);

    List<TeamMember> teamMembers = teamMapper.findTeamMembersByLectureId(lectureId);

    HashMap<Long, List<StudentInfoResponseDto>> teamStudentsMap = new HashMap<>();
    HashSet<Long> assignedStudentIds = new HashSet<>();

    for (TeamMember member : teamMembers) {
      Long studentId = member.getStudentId();
      StudentInfoResponseDto studentDto = studentDtoMap.get(studentId);

      if (studentDto != null) {

        teamStudentsMap.computeIfAbsent(member.getTeamId(), k -> new ArrayList<>())
            .add(studentDto);
        assignedStudentIds.add(studentId);
      }

    }
    List<TeamResponseDto> resultList = teams.stream()
        .map(team -> TeamResponseDto.of(
            team.getId(),
            team.getName(),
            lectureId,
            teamStudentsMap.getOrDefault(team.getId(), Collections.emptyList())
        ))
        .collect(Collectors.toList());

    List<StudentInfoResponseDto> unassignedStudents = studentIds.stream()
        .filter(studentId -> !assignedStudentIds.contains(studentId))
        .map(studentDtoMap::get)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    if (!unassignedStudents.isEmpty()) {
      resultList.add(TeamResponseDto.of(null, "미배정", lectureId, unassignedStudents));
    }

    return resultList;
  }


  public TeamResponseDto getTeamByTeamId(Long lectureId, Long teamId, AuthInfo authInfo) {

    validateLectureAccessForInstructor(lectureId, authInfo);

    validateTeam(teamId, lectureId);
    Team team = teamMapper.findTeamByTeamId(teamId);

    List<TeamMember> teamMembers = teamMapper.findTeamMembersByTeamId(teamId);

    if (teamMembers.isEmpty()) {
      return TeamResponseDto.of(teamId, team.getName(), lectureId, Collections.emptyList());
    }

    List<Long> studentIds = teamMembers.stream()
        .map(TeamMember::getStudentId)
        .toList();

    Map<Long, StudentInfoResponseDto> studentDtoMap = memberMapper.findStudentInfoMapByIds(
        studentIds);

    List<StudentInfoResponseDto> students = teamMembers.stream()
        .map(member -> studentDtoMap.get(member.getStudentId()))
        .filter(Objects::nonNull)
        .toList();

    return TeamResponseDto.of(team.getId(), team.getName(), lectureId, students);
  }


  public TeamResponseDto getMyTeamInfoInLecture(Long lectureId, AuthInfo authInfo) {
    validateLectureAccessForStudent(lectureId, authInfo);

    Long studentId = authInfo.getStudentId();

    TeamMember myTeamMember = teamMapper.findTeamMemberByLectureIdAndStudentId(lectureId,
        studentId);

    if (myTeamMember == null) {
      return TeamResponseDto.of(null, "미배정", lectureId, List.of());
    }

    Team team = teamMapper.findTeamByTeamId(myTeamMember.getTeamId());

    List<TeamMember> teamMembers = teamMapper.findTeamMembersByTeamId(team.getId());

    List<Long> studentIds = teamMembers.stream()
        .map(TeamMember::getStudentId)
        .toList();

    Map<Long, StudentInfoResponseDto> studentDtoMap = memberMapper.findStudentInfoMapByIds(
        studentIds);

    List<StudentInfoResponseDto> students = teamMembers.stream()
        .map(member -> studentDtoMap.get(member.getStudentId()))
        .filter(Objects::nonNull)
        .toList();

    return TeamResponseDto.of(team.getId(), team.getName(), lectureId, students);
  }


  private void validateLectureAccessForInstructor(long lectureId, AuthInfo authInfo) {
    Lecture lecture = lectureMapper.findLectureById(lectureId);
    if (lecture == null) {
      throw new IllegalArgumentException("수업을 찾을 수 없습니다");
    }

    if (!authInfo.isInstructor()) {
      throw new IllegalArgumentException("접근 권한이 없습니다 (역할 아님)");
    }

    if (!Objects.equals(lecture.getInstructorId(), authInfo.getInstructorId())) {
      throw new IllegalArgumentException("해당 수업에 대한 접근 권한이 없습니다");
    }
  }


  private void validateLectureAccessForStudent(long lectureId, AuthInfo authInfo) {
    if (!authInfo.isStudent()) {
      throw new IllegalArgumentException("접근 권한이 없습니다 (역할 아님)");
    }

    boolean isEnrolled = lectureMapper.existEnrollmentByStudentIdAndLectureID(
        authInfo.getStudentId(), lectureId);

    if (!isEnrolled) {
      throw new IllegalArgumentException("해당 수업에 등록되지 않았습니다");
    }
  }


  private void validateTeam(Long teamId, Long lectureId) {
    Team targetTeam = teamMapper.findTeamByTeamId(teamId);
    if (targetTeam == null || !Objects.equals(targetTeam.getLectureId(), lectureId)) {
      throw new IllegalArgumentException("유효하지 않은 팀.");
    }
  }

  private List<Long> getEnrolledStudentIds(Long lectureId) {
    return lectureMapper.findStudentIdsByLectureId(lectureId);
  }

}
