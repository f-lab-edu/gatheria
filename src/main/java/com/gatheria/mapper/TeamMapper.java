package com.gatheria.mapper;

import com.gatheria.domain.Team;
import com.gatheria.domain.TeamMember;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeamMapper {

  List<Team> findTeamsByLectureId(Long lectureId);

  void saveTeams(List<Team> newTeams);

  void saveTeamMembers(List<TeamMember> teamMembers);

  TeamMember findTeamMemberByStudentIdAndLectureId(Long studentId, Long lectureId);

  void updateTeamMember(TeamMember existingMember);

  void saveTeamMember(TeamMember newMember);

  List<TeamMember> findTeamMembersByLectureId(Long lectureId);

  List<TeamMember> findTeamMembersByTeamId(Long teamId);

  TeamMember findTeamMemberByLectureIdAndStudentId(Long lectureId, Long studentId);

  Team findTeamByTeamId(Long teamId);
}
