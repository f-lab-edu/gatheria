package com.gatheria.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponseDto {

  private Long teamId;
  private String teamName;
  private Long lectureId;
  private List<StudentInfoResponseDto> students;

  public static TeamResponseDto of(Long teamId, String teamName, Long lectureId,
      List<StudentInfoResponseDto> students) {
    return TeamResponseDto.builder()
        .teamId(teamId)
        .teamName(teamName)
        .lectureId(lectureId)
        .students(students)
        .build();
  }
}
