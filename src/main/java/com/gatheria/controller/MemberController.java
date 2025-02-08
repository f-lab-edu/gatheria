package com.gatheria.controller;


import com.gatheria.dto.request.InstructorRegisterRequestDto;
import com.gatheria.dto.request.StudentRegisterRequestDto;
import com.gatheria.dto.response.EmailCheckResponseDto;
import com.gatheria.dto.response.InstructorRegisterResponseDto;
import com.gatheria.dto.response.StudentRegisterResponseDto;
import com.gatheria.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/instructor/register")
    public ResponseEntity<?> registerUser(@RequestBody InstructorRegisterRequestDto request) {
        InstructorRegisterResponseDto response = memberService.register(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/student/register")
    public ResponseEntity<?> registerUser(@RequestBody StudentRegisterRequestDto request) {
        StudentRegisterResponseDto response = memberService.register(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email-check")
    public ResponseEntity<?> checkEmailDuplicate(
            @RequestParam String email) {
        boolean isDuplicate = memberService.emailExists(email);
        EmailCheckResponseDto response = EmailCheckResponseDto.of(isDuplicate);
        return ResponseEntity.ok(response);
    }
}
