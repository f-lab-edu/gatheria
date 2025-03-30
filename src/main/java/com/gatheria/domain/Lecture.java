package com.gatheria.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Lecture extends BaseEntity {

  private String name;
  private String code;
  private Long instructorId;
  private Integer classSize;

  protected Lecture(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String name,
      String code, Long instructorId, Integer classSize) {
    super(id, createdAt, updatedAt);
    this.name = name;
    this.code = code;
    this.instructorId = instructorId;
    this.classSize = classSize;
  }

  private Lecture(String name, Long instructorId, Integer classSize) {
    super(null, LocalDateTime.now(), LocalDateTime.now());
    this.name = name;
    this.code = generateCode();
    this.instructorId = instructorId;
    this.classSize = classSize;
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
