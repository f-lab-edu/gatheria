package com.gatheria.dto.response;

import com.gatheria.domain.Notice;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {

  private Long id;
  private Long lectureId;
  private Long instructorId;
  private String title;
  private String contents;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static NoticeResponseDto from(Notice notice) {
    return NoticeResponseDto.builder()
        .id(notice.getId())
        .lectureId(notice.getLectureId())
        .instructorId(notice.getInstructorId())
        .title(notice.getTitle())
        .contents(notice.getContents())
        .createdAt(notice.getCreatedAt())
        .updatedAt(notice.getUpdatedAt())
        .build();
  }
}
