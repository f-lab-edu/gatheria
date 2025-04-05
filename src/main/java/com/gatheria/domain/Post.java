package com.gatheria.domain;

import com.gatheria.dto.response.PostListResponseDto;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Post extends BaseEntity {

  private Long lectureId;
  private Long instructorId;
  private PostType type;
  private String title;
  private String contents;


  public enum PostType {
    NOTICE, ASSIGNMENT
  }

  protected Post(Long id, LocalDateTime createdAt, LocalDateTime updatedAt,
      Long lectureId, Long instructorId, PostType type, String title, String contents) {
    super(id, createdAt, updatedAt);
    this.lectureId = lectureId;
    this.instructorId = instructorId;
    this.type = type;
    this.title = title;
    this.contents = contents;
  }

  public abstract PostListResponseDto createListDto();

  public boolean isLecture(Long lectureId) {
    return this.lectureId != null && this.lectureId.equals(lectureId);
  }

  public boolean isInstructor(Long instructorId) {
    return this.instructorId != null && this.instructorId.equals(instructorId);

  }
}