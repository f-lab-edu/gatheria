package com.gatheria.controller;

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
    public ResponseEntity<?> getPendingInstructors(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(memberService.getPendingInstructors(page, size));
    }

    @PostMapping("/approve-instructor/{id}")
    public ResponseEntity<?> approveInstructor(@PathVariable Long id) {
        memberService.approveInstructor(id);
        return ResponseEntity.ok("Approve instructor");
    }


}
