package com.gatheria.domain;

import com.gatheria.domain.type.MentoringStatus;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class MentoringSession {

  private Long id;
  private final String title;
  private final String mentorName;
  private final LocalDateTime sessionDate;
  private final LocalDateTime waitingStartDate;
  private final LocalDateTime waitingEndDate;
  private final Integer maxParticipants;
  private Integer currentParticipants;
  private MentoringStatus status;
  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private MentoringSession(String title, String mentorName, LocalDateTime sessionDate,
      LocalDateTime waitingStartDate, LocalDateTime waitingEndDate, Integer maxParticipants) {
    this.title = title;
    this.mentorName = mentorName;
    this.sessionDate = sessionDate;
    this.waitingStartDate = waitingStartDate;
    this.waitingEndDate = waitingEndDate;
    this.maxParticipants = maxParticipants;
    this.currentParticipants = 0;
    this.status = MentoringStatus.SCHEDULED;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }


  public static MentoringSession of(String title, String mentorName, LocalDateTime sessionDate,
      LocalDateTime waitingStartDate, LocalDateTime waitingEndDate, Integer maxParticipants) {
    return new MentoringSession(title, mentorName, sessionDate, waitingStartDate, waitingEndDate,
        maxParticipants);
  }

  public void incrementCurrentParticipants() {
    if (this.currentParticipants >= this.maxParticipants) {
      throw new IllegalStateException("정원 초과");
    }
    this.currentParticipants++;
    this.updatedAt = LocalDateTime.now();

    if (Objects.equals(this.currentParticipants, this.maxParticipants)) {
      this.status = MentoringStatus.WAITING_CLOSED;
    }
  }

  public void decrementCurrentParticipants() {
    if (this.currentParticipants > 0) {
      this.currentParticipants--;
      this.updatedAt = LocalDateTime.now();
    }

    if (this.status == MentoringStatus.WAITING_CLOSED) {
      this.status = MentoringStatus.WAITING_OPEN;
    }

    //TODO : 등록 대기 줄이 있는 상황 처리 (바로 wait open으로 변경하지 않고 대기줄이 있는 경우 처리)
  }

  public void cancel() {
    if (this.status == MentoringStatus.COMPLETED) {
      throw new IllegalStateException("이미 종료된 세션");
    }
    this.status = MentoringStatus.CANCELLED;
    this.updatedAt = LocalDateTime.now();
  }

  public boolean canJoin() {
    return this.status == MentoringStatus.WAITING_OPEN
        && this.currentParticipants < this.maxParticipants;
  }

  public void updateStateAuto() {
    //TODO : 자동 상태 변경 로직이 여기서 있으면 언제 상태가 변하는건지 알기 어려운데 어디서 관리해야하는가
  }
}
