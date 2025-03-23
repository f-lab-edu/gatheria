package com.gatheria.domain.type;

import lombok.Getter;

@Getter
public enum MentoringStatus {
  SCHEDULED,
  WAITING_OPEN,
  WAITING_CLOSED,
  COMPLETED,
  CANCELLED
}
