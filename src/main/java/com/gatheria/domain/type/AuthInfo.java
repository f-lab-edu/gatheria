package com.gatheria.domain.type;

import lombok.Getter;

@Getter
public class AuthInfo {

  private final String email;
  private final MemberRole role;
  private final Long memberId;
  private final Long studentId;
  private final Long instructorId;

  public AuthInfo(String email, MemberRole role, Long memberId, Long studentId, Long instructorId) {
    this.email = email;
    this.role = role;
    this.memberId = memberId;
    this.studentId = studentId;
    this.instructorId = instructorId;
  }

  public boolean isInstructor() {
    return this.role == MemberRole.INSTRUCTOR;
  }

  public boolean isStudent() {
    return this.role == MemberRole.STUDENT;
  }

  public void validateInstructor() {
    if (!isInstructor()) {
      throw new IllegalArgumentException("강사만 접근 가능합니다.");
    }
  }

  public void validateStudent() {
    if (!isStudent()) {
      throw new IllegalArgumentException("학생만 접근 가능합니다.");
    }
  }
}
