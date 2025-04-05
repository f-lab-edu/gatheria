package com.gatheria.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FileAttachment extends BaseEntity {

  private String fileName;
  private String filePath;
  private Long fileSize;
  private Long postId;
  private Long submissionId;

  public static FileAttachment of(String fileName, String filePath, Long fileSize) {
    return FileAttachment.builder()
        .fileName(fileName)
        .filePath(filePath)
        .fileSize(fileSize)
        .build();
  }
}