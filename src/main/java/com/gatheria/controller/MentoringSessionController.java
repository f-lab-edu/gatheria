package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.MentoringSessionCreateRequestDto;
import com.gatheria.dto.response.MentoringSessionCreateResponseDto;
import com.gatheria.dto.response.SessionRegistrationResponseDto;
import com.gatheria.service.MentoringSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentoring")
public class MentoringSessionController {

  private final MentoringSessionService mentoringSessionService;

  private static final String ADMIN_KEY = "admin123";

  public MentoringSessionController(MentoringSessionService mentoringSessionService) {
    this.mentoringSessionService = mentoringSessionService;
  }

  @PostMapping("/create")
  public ResponseEntity<MentoringSessionCreateResponseDto> createMentoringSession(
      @RequestBody MentoringSessionCreateRequestDto request,
      @RequestParam String adminKey) {

    boolean isAdmin = ADMIN_KEY.equals(adminKey);

    if (!isAdmin) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    MentoringSessionCreateResponseDto response = mentoringSessionService.createMentoringSession(
        request);

    return ResponseEntity.ok(response);
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
