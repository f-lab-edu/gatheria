package com.gatheria.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LectureStudent extends BaseEntity {

  private Long lectureId;
  private Long studentId;


  protected LectureStudent(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
      Long lectureId, Long studentId) {
    super(id, createdAt, updatedAt);
    this.lectureId = lectureId;
    this.studentId = studentId;
  }

  public static LectureStudent of(Long lectureId, Long studentId) {
    return LectureStudent.builder()
        .lectureId(lectureId)
        .studentId(studentId)
        .build();
  }
}
