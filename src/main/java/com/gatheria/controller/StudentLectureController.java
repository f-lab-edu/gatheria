package com.gatheria.controller;

import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.LectureJoinRequest;
import com.gatheria.dto.response.LectureJoinResponse;
import com.gatheria.dto.response.LectureResponseDto;
import com.gatheria.service.LectureService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
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
      @RequestAttribute("authInfo") AuthInfo authInfo) {
    List<LectureResponseDto> lectures = lectureService.getLecturesByStudentId(authInfo);
    return ResponseEntity.ok(lectures);
  }

  @PostMapping("/join")
  public ResponseEntity<LectureJoinResponse> joinLecture(@RequestBody LectureJoinRequest request,
      @RequestAttribute("authInfo") AuthInfo authInfo) {
    LectureJoinResponse response = lectureService.joinLecture(request.getCode(), authInfo);
    return ResponseEntity.ok(response);
  }


}
