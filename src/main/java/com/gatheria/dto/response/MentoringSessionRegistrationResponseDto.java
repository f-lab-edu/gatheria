package com.gatheria.dto.response;

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

  public static MentoringSessionRegistrationResponseDto success(
      Long sessionId,
      String sessionTitle,
      LocalDateTime sessionDate) {

    return new MentoringSessionRegistrationResponseDto(
        true,
        sessionId,
        sessionTitle,
        sessionDate,
        null,
        HttpStatus.OK
    );
  }

  public static MentoringSessionRegistrationResponseDto fail(String message, HttpStatus status) {
    return new MentoringSessionRegistrationResponseDto(false, null, null, null, message, status);
  }
}