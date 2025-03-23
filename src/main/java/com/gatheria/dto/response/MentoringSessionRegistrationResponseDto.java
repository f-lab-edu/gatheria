package com.gatheria.dto.response;

import com.gatheria.domain.type.SessionParticipantStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentoringSessionRegistrationResponseDto {

  private boolean success;
  private Long sessionId;
  private String sessionTitle;
  private LocalDateTime sessionDate;
  private String message;
  private HttpStatus status;
  private SessionParticipantStatus participantStatus;
  private Long participantId;

  public static MentoringSessionRegistrationResponseDto success(
      Long sessionId,
      String sessionTitle,
      LocalDateTime sessionDate,
      Long participantId) {

    return new MentoringSessionRegistrationResponseDto(
        true,
        sessionId,
        sessionTitle,
        sessionDate,
        null,
        HttpStatus.OK,
        SessionParticipantStatus.REGISTERED,
        participantId
    );
  }

  // 실패 응답 생성 (참가자 ID 없음)
  public static MentoringSessionRegistrationResponseDto fail(
      String message,
      HttpStatus status) {
    return new MentoringSessionRegistrationResponseDto(
        false,
        null,
        null,
        null,
        message,
        status,
        null,
        null
    );
  }

  public static MentoringSessionRegistrationResponseDto rejected(
      String message,
      HttpStatus status,
      Long sessionId,
      Long participantId) {
    return new MentoringSessionRegistrationResponseDto(
        false,
        sessionId,
        null,
        null,
        message,
        status,
        SessionParticipantStatus.REJECTED,
        participantId
    );
  }
}