package com.gatheria.mapper;

import com.gatheria.domain.Team;
import com.gatheria.domain.TeamMember;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TeamMapper {

  @Select("SELECT * FROM teams WHERE lecture_id = #{lectureId}")
  List<Team> findTeamsByLectureId(@Param("lectureId") Long lectureId);


  void saveTeams(List<Team> newTeams);

  void saveTeamMembers(List<TeamMember> teamMembers);

  @Select("SELECT * FROM team_members WHERE lecture_id = #{lectureId}")
  List<TeamMember> findTeamMembersByLectureId(@Param("lectureId") Long lectureId);

  @Select("SELECT * FROM team_members WHERE student_id = #{studentId} AND lecture_id = #{lectureId}")
  TeamMember findTeamMemberByStudentIdAndLectureId(@Param("studentId") Long studentId,
      @Param("lectureId") Long lectureId);


  @Update("UPDATE team_members SET team_id = #{teamId}, updated_at = NOW() WHERE id = #{id}")
  void updateTeamMember(TeamMember teamMember);


  @Select("SELECT * FROM teams WHERE id = #{teamId}")
  Team findTeamByTeamId(@Param("teamId") Long teamId);

  @Select("SELECT * FROM team_members WHERE team_id = #{teamId}")
  List<TeamMember> findTeamMembersByTeamId(@Param("teamId") Long teamId);
}
