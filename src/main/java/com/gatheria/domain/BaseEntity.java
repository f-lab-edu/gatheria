package com.gatheria.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class BaseEntity {

  protected Long id;
  protected LocalDateTime createdAt;
  protected LocalDateTime updatedAt;

  protected BaseEntity(Long id, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
