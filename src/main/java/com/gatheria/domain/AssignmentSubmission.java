package com.gatheria.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AssignmentSubmission extends BaseEntity {

  private Long assignmentId;
  private Long studentId;
  private String content;
  private Integer score;

  @Builder.Default
  private List<FileAttachment> attachments = new ArrayList<>();

  public static AssignmentSubmission of(Long assignmentId, Long studentId, String content) {
    return AssignmentSubmission.builder()
        .assignmentId(assignmentId)
        .studentId(studentId)
        .content(content)
        .build();
  }

  public static AssignmentSubmission of(Long assignmentId, Long studentId,
      String content, List<FileAttachment> attachments) {
    return AssignmentSubmission.builder()
        .assignmentId(assignmentId)
        .studentId(studentId)
        .content(content)
        .attachments(attachments)
        .build();
  }
}
