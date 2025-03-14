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
public class SessionRegistrationResponseDto {

  private boolean success;
  private Long sessionId;
  private String sessionTitle;
  private LocalDateTime sessionDate;
  private String message;
  private HttpStatus status;

  public static SessionRegistrationResponseDto success(
      Long sessionId,
      String sessionTitle,
      LocalDateTime sessionDate) {

    return new SessionRegistrationResponseDto(
        true,
        sessionId,
        sessionTitle,
        sessionDate,
        "멘토링 세션 등록 완료.",
        HttpStatus.OK
    );
  }

  public static SessionRegistrationResponseDto fail(String message, HttpStatus status) {
    return new SessionRegistrationResponseDto(false, null, null, null, message, status);
  }
}