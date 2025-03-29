package com.gatheria.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamMembers {

  private final List<TeamMember> members;

  private TeamMembers(List<TeamMember> members) {
    this.members = new ArrayList<>(members);
  }

  public static TeamMembers create(List<Team> teams, List<Long> studentIds, Long lectureId,
      int teamSize) {

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

    return new TeamMembers(teamMembers);
  }

  public List<TeamMember> getMembers() {
    return Collections.unmodifiableList(members);
  }
}
