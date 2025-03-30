package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.LectureJoinRequestDto;
import com.gatheria.dto.response.LectureJoinResponseDto;
import com.gatheria.dto.response.LectureResponseDto;
import com.gatheria.service.LectureService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/lectures")
public class StudentLectureController {

  private final LectureService lectureService;

  @Autowired
  public StudentLectureController(LectureService lectureService) {
    this.lectureService = lectureService;
  }

  @GetMapping
  public ResponseEntity<List<LectureResponseDto>> showLectureList(
      @Auth AuthInfo authInfo) {
    List<LectureResponseDto> lectures = lectureService.getLectureListByStudentId(authInfo);
    return ResponseEntity.ok(lectures);
  }

  @PostMapping("/join")
  public ResponseEntity<LectureJoinResponseDto> joinLecture(
      @RequestBody LectureJoinRequestDto request,
      @Auth AuthInfo authInfo) {
    LectureJoinResponseDto response = lectureService.joinLecture(request.getCode(), authInfo);
    return ResponseEntity.ok(response);
  }
}