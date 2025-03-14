package com.gatheria.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Lecture {

  private Long id;
  private String name;
  private String code;
  private Long instructorId;
  private Integer classSize;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Lecture(String name, Long instructorId, Integer classSize) {
    this.name = name;
    this.code = generateCode();
    this.instructorId = instructorId;
    this.classSize = classSize;
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }

  private String generateCode() {
    return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
  }

  public static Lecture of(String name, Long instructorId, Integer classSize) {
    return new Lecture(name, instructorId, classSize);
  }

  public boolean isOwnedBy(Long memberId) {
    return Objects.equals(this.instructorId, memberId);
  }
}
