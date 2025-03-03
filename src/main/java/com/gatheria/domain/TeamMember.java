package com.gatheria.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

  private Long id;
  private Long teamId;
  private Long studentId;
  private Long lectureId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static TeamMember of(Long teamId, Long studentId, Long lectureId) {
    return new TeamMember(
        null,
        teamId,
        studentId,
        lectureId,
        LocalDateTime.now(),
        LocalDateTime.now()
    );
  }

  public void updateTeamId(Long newTeamId) {
    this.teamId = newTeamId;
    this.updatedAt = LocalDateTime.now();
  }
}
