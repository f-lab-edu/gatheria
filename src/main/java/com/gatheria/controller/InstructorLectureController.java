package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.LectureCreateRequestDto;
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
@RequestMapping("/api/instructor/lectures")
public class InstructorLectureController {

  private final LectureService lectureService;

  @Autowired
  public InstructorLectureController(LectureService lectureService) {
    this.lectureService = lectureService;
  }

  @PostMapping
  public ResponseEntity<Void> createLecture(
      @RequestBody LectureCreateRequestDto request,
      @Auth AuthInfo authInfo) {
    lectureService.createLecture(request, authInfo);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<LectureResponseDto>> showLectureList(
      @Auth AuthInfo authInfo) {
    List<LectureResponseDto> lectures = lectureService.getLectureListByInstructorId(authInfo);
    return ResponseEntity.ok(lectures);
  }
}
