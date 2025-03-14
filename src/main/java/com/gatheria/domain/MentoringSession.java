package com.gatheria.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MentoringSession {

  private Long id;
  private final String title;
  private final String mentorName;
  private final LocalDateTime sessionDate;
  private final Integer maxParticipants;
  private Integer currentParticipants;
  private String status;
  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private MentoringSession(String title, String mentorName, LocalDateTime sessionDate,
      Integer maxParticipants) {
    this.title = title;
    this.mentorName = mentorName;
    this.sessionDate = sessionDate;
    this.maxParticipants = maxParticipants;
    this.currentParticipants = 0;
    this.status = "OPEN";
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  public static MentoringSession of(String title, String mentorName, LocalDateTime sessionDate,
      Integer maxParticipants) {
    return new MentoringSession(title, mentorName, sessionDate, maxParticipants);
  }

  public void incrementCurrentParticipants() {
    if (this.currentParticipants >= this.maxParticipants) {
      throw new IllegalStateException("정원 초과");
    }
    this.currentParticipants++;
    this.updatedAt = LocalDateTime.now();
  }

  //TODO : 현재 참석자 수 감소

  //TODO : 멘토링 세션 취소

  //TODO : 지금 가입할 수 있는가?
}
