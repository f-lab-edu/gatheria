package com.gatheria.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureStudent {

  private Long id;
  private Long lectureId;
  private Long studentId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static LectureStudent create(Long lectureId, Long studentId) {
    return LectureStudent.builder()
        .lectureId(lectureId)
        .studentId(studentId)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }
}
