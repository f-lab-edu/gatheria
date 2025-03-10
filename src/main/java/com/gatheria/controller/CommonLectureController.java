package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.response.LectureResponseDto;
import com.gatheria.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lectures")
public class CommonLectureController {

  private final LectureService lectureService;

  @Autowired
  public CommonLectureController(LectureService lectureService) {
    this.lectureService = lectureService;
  }

  @GetMapping("/{lectureId}")
  public ResponseEntity<LectureResponseDto> showLecture(@PathVariable Long lectureId,
      @Auth AuthInfo authInfo) {

    LectureResponseDto lecture = lectureService.findLectureByCodeAndId(lectureId, authInfo);
    return ResponseEntity.ok(lecture);

  }

}
