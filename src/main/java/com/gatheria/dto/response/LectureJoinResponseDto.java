package com.gatheria.dto.response;

import com.gatheria.domain.Lecture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LectureJoinResponseDto {

  private Long id;
  private String code;
  private String name;

  public static LectureJoinResponseDto from(Lecture lecture) {
    return LectureJoinResponseDto.builder()
        .id(lecture.getId())
        .code(lecture.getCode())
        .name(lecture.getName())
        .build();
  }
}
