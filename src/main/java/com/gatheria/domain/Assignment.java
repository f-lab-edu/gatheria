package com.gatheria.domain;

import com.gatheria.dto.response.PostListResponseDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class Assignment extends Post {

  private LocalDateTime dueDate;

  public static Assignment of(Long lectureId, Long instructorId, String title,
      String contents, LocalDateTime dueDate) {
    return Assignment.builder()
        .lectureId(lectureId)
        .instructorId(instructorId)
        .type(PostType.ASSIGNMENT)
        .title(title)
        .contents(contents)
        .dueDate(dueDate)
        .build();
  }

  public static Assignment of(Long id, Long lectureId, Long instructorId,
      String title, String contents, LocalDateTime dueDate) {
    return Assignment.builder()
        .id(id)
        .lectureId(lectureId)
        .instructorId(instructorId)
        .title(title)
        .contents(contents)
        .dueDate(dueDate)
        .type(PostType.ASSIGNMENT)
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
        .dueDate(getDueDate())
        .build();
  }
}
