package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.response.MentoringSessionRegistrationResponseDto;
import com.gatheria.dto.response.MentoringSessionResponseDto;
import com.gatheria.service.MentoringSessionService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping("/all")
  public ResponseEntity<List<MentoringSessionResponseDto>> getSessions() {
    return ResponseEntity.ok(mentoringSessionService.getAllSessions());
  }

  @GetMapping("/{sessionId}")
  public ResponseEntity<MentoringSessionResponseDto> getSession(@PathVariable Long sessionId) {
    return ResponseEntity.ok(mentoringSessionService.getSession(sessionId));
  }

  @GetMapping("/my")
  public ResponseEntity<List<MentoringSessionResponseDto>> getMySessions(@Auth AuthInfo authInfo) {
    return ResponseEntity.ok(mentoringSessionService.getMySessions(authInfo));
  }

  @PostMapping("/{sessionId}/join")
  public ResponseEntity<MentoringSessionRegistrationResponseDto> joinMentoringSession(
      @PathVariable Long sessionId,
      @Auth AuthInfo authInfo) {
    LocalDateTime requestTime = LocalDateTime.now();
    MentoringSessionRegistrationResponseDto response = mentoringSessionService.registerSession(
        sessionId, authInfo, requestTime);

    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
