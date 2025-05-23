package com.gatheria.domain;

import com.gatheria.domain.type.SessionParticipantStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionParticipant {

  private Long id;
  private Long sessionId;
  private Long studentId;
  private LocalDateTime requestAt;
  private LocalDateTime registeredAt;
  private SessionParticipantStatus status;
  private LocalDateTime cancelledAt;
  private LocalDateTime rejectedAt;

  private SessionParticipant(Long sessionId, Long studentId, LocalDateTime requestAt) {
    this.sessionId = sessionId;
    this.studentId = studentId;
    this.requestAt = requestAt;
    this.registeredAt = null;
    this.status = SessionParticipantStatus.REGISTERED;
    this.cancelledAt = null;
    this.rejectedAt = null;
  }

  public static SessionParticipant of(Long sessionId, Long studentId, LocalDateTime requestAt) {
    return new SessionParticipant(sessionId, studentId, requestAt);
  }

  public void cancel() {
    this.status = SessionParticipantStatus.CANCELLED;
    this.cancelledAt = LocalDateTime.now();
  }

  public void completeRegistration() {
    this.registeredAt = LocalDateTime.now();
  }

  public void reject() {
    this.status = SessionParticipantStatus.REJECTED;
    this.rejectedAt = LocalDateTime.now();
  }
}