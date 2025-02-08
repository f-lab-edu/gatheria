package com.gatheria.controller;

import com.gatheria.dto.response.PagedInstructorResponseDto;
import com.gatheria.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final MemberService memberService;

    @Autowired
    public AdminController(MemberService memberService) {
        this.memberService = memberService;
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


}
