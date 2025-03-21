package com.gatheria.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Team extends BaseEntity {

  private String name;
  private Long lectureId;

  protected Team(Long id, String name, Long lectureId, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(id, createdAt, updatedAt);
    this.name = name;
    this.lectureId = lectureId;
  }

  public static Team of(String name, Long lectureId) {
    return Team.builder()
        .name(name)
        .lectureId(lectureId)
        .build();
  }

  public static List<Team> createTeams(Long lectureId, int teamCount) {
    List<Team> teams = new ArrayList<>();
    for (int i = 0; i < teamCount; i++) {
      String teamName = String.valueOf('A' + i);
      teams.add(Team.of(teamName, lectureId));
    }
    return teams;
  }
}
