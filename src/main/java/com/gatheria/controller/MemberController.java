package com.gatheria.controller;


import com.gatheria.dto.request.InstructorRegisterRequestDto;
import com.gatheria.dto.request.StudentRegisterRequestDto;
import com.gatheria.dto.response.InstructorRegisterResponseDto;
import com.gatheria.dto.response.StudentRegisterResponseDto;
import com.gatheria.service.InstructorService;
import com.gatheria.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final InstructorService instructorService;
    private final StudentService studentService;

    @Autowired
    public MemberController(InstructorService instructorService, StudentService studentService) {
        this.instructorService = instructorService;
        this.studentService = studentService;
    }

    @PostMapping("/instructor/register")
    public ResponseEntity<?> registerUser(@RequestBody InstructorRegisterRequestDto request) {
        InstructorRegisterResponseDto response = instructorService.register(request);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/student/register")
    public ResponseEntity<?> registerUser(@RequestBody StudentRegisterRequestDto request) {
        StudentRegisterResponseDto response = studentService.register(request);
        return ResponseEntity.ok(response);
    }
}
