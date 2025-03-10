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

  private Long id;
  private String name;
  private Long lectureId;
  private List<StudentResponseDto> students;

  public static TeamResponseDto of(Long id, String name, Long lectureId,
      List<StudentResponseDto> students) {
    return TeamResponseDto.builder()
        .id(id)
        .name(name)
        .lectureId(lectureId)
        .students(students)
        .build();
  }
}
