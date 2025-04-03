package com.gatheria.dto.response;

import com.gatheria.domain.Post;
import com.gatheria.domain.Post.PostType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDto {

  private Long id;
  private Long lectureId;
  private Long instructorId;
  private PostType type;
  private String title;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime dueDate; //과제만 이 값을 가짐 체크

  public static PostListResponseDto from(Post post) {
    return post.createListDto();
  }
}
