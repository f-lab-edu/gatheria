package com.gatheria.controller;

import com.gatheria.dto.request.AdminRegisterRequestDto;
import com.gatheria.dto.request.MentoringSessionCreateRequestDto;
import com.gatheria.dto.response.AdminRegisterResponseDto;
import com.gatheria.dto.response.MentoringSessionCreateResponseDto;
import com.gatheria.dto.response.PagedInstructorResponseDto;
import com.gatheria.service.AdminService;
import com.gatheria.service.MemberService;
import com.gatheria.service.MentoringSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final AdminService adminService;
  private final MentoringSessionService mentoringSessionService;
  private final MemberService memberService;

  @Autowired
  public AdminController(AdminService adminService, MentoringSessionService mentoringSessionService,
      MemberService memberService) {
    this.adminService = adminService;
    this.mentoringSessionService = mentoringSessionService;
    this.memberService = memberService;
  }

  @PostMapping("/register")
  public ResponseEntity<AdminRegisterResponseDto> registerAdmin(
      @RequestBody AdminRegisterRequestDto request) {
    AdminRegisterResponseDto response = adminService.register(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestParam String username,
      @RequestParam String password) {
    boolean success = adminService.login(username, password);
    if (success) {
      return ResponseEntity.ok("로그인 성공");
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
  }


  @GetMapping("/pending-instructors")
  public ResponseEntity<PagedInstructorResponseDto> showPendingInstructors(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size) {
    PagedInstructorResponseDto response = memberService.showPendingInstructors(page, size);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/approve-instructor/{id}")
  public ResponseEntity<Void> approveInstructor(@PathVariable Long id) {
    memberService.approveInstructor(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/create-mentoring-session")
  public ResponseEntity<MentoringSessionCreateResponseDto> createMentoringSession(
      @RequestBody MentoringSessionCreateRequestDto request) {

    MentoringSessionCreateResponseDto response = mentoringSessionService.createMentoringSession(
        request);

    return ResponseEntity.ok(response);
  }
}
