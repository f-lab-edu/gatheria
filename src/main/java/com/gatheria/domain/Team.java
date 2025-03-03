package com.gatheria.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

  private Long id;
  private String name;
  private Long lectureId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static Team of(String name, Long lectureId) {
    return new Team(
        null,
        name,
        lectureId,
        LocalDateTime.now(),
        LocalDateTime.now()
    );
  }
}
