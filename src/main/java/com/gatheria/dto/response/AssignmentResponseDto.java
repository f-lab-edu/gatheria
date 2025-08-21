package com.gatheria.dto.response;


import com.gatheria.domain.Assignment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponseDto {

  private Long id;
  private Long lectureId;
  private Long instructorId;
  private String title;
  private String contents;
  private LocalDateTime dueDate;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static AssignmentResponseDto from(Assignment assignment) {
    return AssignmentResponseDto.builder()
        .id(assignment.getId())
        .lectureId(assignment.getLectureId())
        .instructorId(assignment.getInstructorId())
        .title(assignment.getTitle())
        .contents(assignment.getContents())
        .dueDate(assignment.getDueDate())
        .createdAt(assignment.getCreatedAt())
        .updatedAt(assignment.getUpdatedAt())
        .build();
  }
}
