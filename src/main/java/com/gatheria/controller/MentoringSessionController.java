package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.response.SessionRegistrationResponseDto;
import com.gatheria.service.MentoringSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentoring")
public class MentoringSessionController {

  private final MentoringSessionService mentoringSessionService;

  public MentoringSessionController(MentoringSessionService mentoringSessionService) {
    this.mentoringSessionService = mentoringSessionService;
  }


  @PostMapping("/{sessionId}/join")
  public ResponseEntity<SessionRegistrationResponseDto> joinMentoringSession(
      @PathVariable Long sessionId,
      @Auth AuthInfo authInfo) {

    SessionRegistrationResponseDto response = mentoringSessionService.registerSession(
        sessionId, authInfo);

    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
