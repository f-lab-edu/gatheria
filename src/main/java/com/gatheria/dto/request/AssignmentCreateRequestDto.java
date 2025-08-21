package com.gatheria.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentCreateRequestDto {

  private String title;
  private String contents;
  private LocalDateTime dueDate;
}
