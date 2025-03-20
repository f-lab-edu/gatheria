package com.gatheria.domain;

import com.gatheria.domain.type.SessionParticipantStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SessionParticipant {

  private Long id;
  private final Long sessionId;
  private final Long studentId;
  private LocalDateTime registrationTime;
  private SessionParticipantStatus status;
  private LocalDateTime cancelledAt;

  private SessionParticipant(Long sessionId, Long studentId) {
    this.sessionId = sessionId;
    this.studentId = studentId;
    this.registrationTime = LocalDateTime.now();
    this.status = SessionParticipantStatus.REGISTERED;
    this.cancelledAt = null;
  }

  public static SessionParticipant of(Long sessionId, Long studentId) {
    return new SessionParticipant(sessionId, studentId);
  }

  public void cancel() {
    this.status = SessionParticipantStatus.CANCELLED;
    this.cancelledAt = LocalDateTime.now();
  }

  public static void reRegister(SessionParticipant existing) {
    existing.status = SessionParticipantStatus.REGISTERED;
    existing.registrationTime = LocalDateTime.now();
    existing.cancelledAt = null;
  }

}