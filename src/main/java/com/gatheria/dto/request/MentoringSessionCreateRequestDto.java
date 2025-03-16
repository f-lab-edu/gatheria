package com.gatheria.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MentoringSessionCreateRequestDto {

  private String title;
  private String mentorName;
  private LocalDateTime sessionDate;
  private LocalDateTime waitingStartDate;
  private LocalDateTime waitingEndDate;
  private Integer maxParticipants;
}
