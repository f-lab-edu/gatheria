package com.gatheria.controller;


import com.gatheria.dto.request.LoginRequestDto;
import com.gatheria.dto.response.LoginResponseDto;
import com.gatheria.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/{role}/login")
  public ResponseEntity<LoginResponseDto> login(@PathVariable("role") String role,
      @RequestBody LoginRequestDto request) {
    LoginResponseDto response = authService.authenticate(request, role);
    return ResponseEntity.ok(response);
  }

  //TODO : email-verification 로직 구현 (현재 가짜 로직 구현)
  @PostMapping("/email-verification")
  public ResponseEntity<?> sendEmailVerification(@RequestParam String email,
      @RequestHeader("Authorization") String token) {
    if (token == null || !token.startsWith("Bearer ")) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 필요합니다.");
    }

    System.out.println("이메일 인증 요청: " + email);
    return ResponseEntity.ok("이메일 인증 요청이 전송되었습니다.");
  }

}
