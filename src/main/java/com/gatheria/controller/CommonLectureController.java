package com.gatheria.controller;

import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.response.LectureResponseDto;
import com.gatheria.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      @RequestParam String code,
      @RequestAttribute("authInfo") AuthInfo authInfo) {

    LectureResponseDto lecture = lectureService.findLectureByCodeAndId(code, lectureId, authInfo);
    return ResponseEntity.ok(lecture);

  }

}
