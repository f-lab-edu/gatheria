package com.gatheria.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Lecture extends BaseEntity {

  private String name;
  private String code;
  private Long instructorId;

  protected Lecture(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String name,
      String code, Long instructorId) {
    super(id, createdAt, updatedAt);
    this.name = name;
    this.code = code;
    this.instructorId = instructorId;
  }

  private Lecture(String name, Long instructorId) {
    super(null, LocalDateTime.now(), LocalDateTime.now());
    this.name = name;
    this.code = generateCode();
    this.instructorId = instructorId;
  }

  private String generateCode() {
    return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
  }

  public static Lecture of(String name, Long instructorId) {
    return new Lecture(name, instructorId);
  }

  public boolean isOwnedBy(Long instructorId) {
    return Objects.equals(this.instructorId, instructorId);
  }
}
