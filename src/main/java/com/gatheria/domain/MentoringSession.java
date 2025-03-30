package com.gatheria.domain;

import com.gatheria.domain.type.MentoringStatus;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentoringSession {

  private Long id;
  private String title;
  private String mentorName;
  private LocalDateTime sessionDate;
  private LocalDateTime waitingStartDate;
  private LocalDateTime waitingEndDate;
  private Integer maxParticipants;
  private Integer currentParticipants;
  private MentoringStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public MentoringSession(Long id, String title, String mentorName,
      LocalDateTime sessionDate, LocalDateTime waitingStartDate,
      LocalDateTime waitingEndDate, Integer maxParticipants) {
    this(id, title, mentorName, sessionDate, waitingStartDate,
        waitingEndDate, maxParticipants, 0, MentoringStatus.SCHEDULED,
        LocalDateTime.now(), LocalDateTime.now());
  }

  public static MentoringSession of(String title, String mentorName, LocalDateTime sessionDate,
      LocalDateTime waitingStartDate, LocalDateTime waitingEndDate, Integer maxParticipants) {
    return new MentoringSession(
        null,
        title,
        mentorName,
        sessionDate,
        waitingStartDate,
        waitingEndDate,
        maxParticipants,
        0,
        MentoringStatus.SCHEDULED,
        LocalDateTime.now(),
        LocalDateTime.now()
    );
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
