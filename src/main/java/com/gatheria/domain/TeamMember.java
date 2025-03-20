package com.gatheria.domain;

import java.time.LocalDateTime;
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
}
