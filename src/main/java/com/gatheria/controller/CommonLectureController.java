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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lectures")
public class CommonLectureController {

  private final LectureService lectureService;

  @Autowired
  public CommonLectureController(LectureService lectureService) {
    this.lectureService = lectureService;
  }

  @GetMapping("/{identifier}")
  public ResponseEntity<LectureResponseDto> showLecture(@PathVariable String identifier,
      @RequestAttribute("authInfo") AuthInfo authInfo) {
    String[] parts = identifier.split("-");
    if (parts.length != 2) {
      throw new RuntimeException();
    }

    String lectureCode = parts[0];
    Long lectureId = Long.parseLong(parts[1]);

    LectureResponseDto lecture = lectureService.findByCodeAndId(lectureCode, lectureId, authInfo);
    return ResponseEntity.ok(lecture);

  }

}
