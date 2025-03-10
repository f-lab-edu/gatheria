package com.gatheria.dto.response;

import com.gatheria.domain.MentoringSession;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentoringSessionCreateResponseDto {

  private Long id;
  private String title;
  private String mentorName;
  private LocalDateTime sessionDate;
  private Integer maxParticipants;

  public static MentoringSessionCreateResponseDto of(MentoringSession session) {
    return new MentoringSessionCreateResponseDto(
        session.getId(),
        session.getTitle(),
        session.getMentorName(),
        session.getSessionDate(),
        session.getMaxParticipants()
    );
  }
}
