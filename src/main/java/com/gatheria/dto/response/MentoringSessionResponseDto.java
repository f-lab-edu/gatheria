package com.gatheria.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gatheria.domain.MentoringSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentoringSessionResponseDto {

  private Long sessionId;
  private String title;
  private String mentorName;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime sessionDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime waitingStartDate;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
  private LocalDateTime waitingEndDate;

  private Integer maxParticipants;
  private Integer currentParticipants;
  private String status;
  private boolean canJoin;

  public static MentoringSessionResponseDto of(MentoringSession session) {
    return new MentoringSessionResponseDto(
        session.getId(),
        session.getTitle(),
        session.getMentorName(),
        session.getSessionDate(),
        session.getWaitingStartDate(),
        session.getWaitingEndDate(),
        session.getMaxParticipants(),
        session.getCurrentParticipants(),
        session.getStatus().name(),
        session.canJoin()
    );
  }

  public static List<MentoringSessionResponseDto> listOf(List<MentoringSession> sessions) {
    return sessions.stream()
        .map(MentoringSessionResponseDto::of)
        .collect(Collectors.toList());
  }


}
