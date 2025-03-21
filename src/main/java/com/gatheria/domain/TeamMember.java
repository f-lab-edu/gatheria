package com.gatheria.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class TeamMember extends BaseEntity {

  private Long teamId;
  private Long studentId;
  private Long lectureId;

  protected TeamMember(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
      Long teamId, Long studentId, Long lectureId) {
    super(id, createdAt, updatedAt);
    this.teamId = teamId;
    this.studentId = studentId;
    this.lectureId = lectureId;
  }

  public static TeamMember of(Long teamId, Long studentId, Long lectureId) {
    return TeamMember.builder()
        .teamId(teamId)
        .studentId(studentId)
        .lectureId(lectureId)
        .build();
  }

  public void updateTeamId(Long newTeamId) {
    this.teamId = newTeamId;
  }

  public static List<TeamMember> createTeamMembers(List<Team> teams, List<Long> studentIds,
      Long lectureId, int teamSize) {
    List<TeamMember> teamMembers = new ArrayList<>();

    int totalStudents = studentIds.size();
    int extraCount = totalStudents % teams.size();

    int studentIdx = 0;
    for (int i = 0; i < teams.size(); i++) {
      int memberCount = (i < extraCount) ? teamSize + 1 : teamSize;
      for (int j = 0; j < memberCount; j++) {
        if (studentIdx < studentIds.size()) {
          Long studentId = studentIds.get(studentIdx++);
          Long teamId = teams.get(i).getId();
          teamMembers.add(TeamMember.of(teamId, studentId, lectureId));
        }
      }
    }

    return teamMembers;

  }

}
