package com.gatheria.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SessionParticipant {

  private Long id;
  private final Long sessionId;
  private final Long studentId;
  private LocalDateTime registrationTime; // final 제거
  private String status;
  private LocalDateTime cancelledAt;

  private SessionParticipant(Long sessionId, Long studentId) {
    this.sessionId = sessionId;
    this.studentId = studentId;
    this.registrationTime = LocalDateTime.now();
    this.status = "REGISTERED";
    this.cancelledAt = null;
  }

  public static SessionParticipant of(Long sessionId, Long studentId) {
    return new SessionParticipant(sessionId, studentId);
  }

  public void cancel() {
    this.status = "CANCELLED";
    this.cancelledAt = LocalDateTime.now();
  }

  public static SessionParticipant reRegister(SessionParticipant existing) {
    existing.status = "REGISTERED";
    existing.registrationTime = LocalDateTime.now();
    existing.cancelledAt = null;
    return existing;
  }

}