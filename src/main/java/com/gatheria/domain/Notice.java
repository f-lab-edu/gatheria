package com.gatheria.domain;

import com.gatheria.dto.response.PostListResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class Notice extends Post {

  public static Notice of(Long lectureId, Long instructorId, String title, String contents) {
    return Notice.builder()
        .lectureId(lectureId)
        .instructorId(instructorId)
        .type(PostType.NOTICE)
        .title(title)
        .contents(contents)
        .build();
  }


  @Override
  public PostListResponseDto createListDto() {
    return PostListResponseDto.builder()
        .id(getId())
        .lectureId(getLectureId())
        .instructorId(getInstructorId())
        .type(getType())
        .title(getTitle())
        .createdAt(getCreatedAt())
        .updatedAt(getUpdatedAt())
        .build();
  }

  public static Notice of(Long id, Long lectureId, Long instructorId, String title,
      String contents) {
    return Notice.builder()
        .id(id)
        .lectureId(lectureId)
        .instructorId(instructorId)
        .type(PostType.NOTICE)
        .title(title)
        .contents(contents)
        .build();
  }

}
