package com.gatheria.domain.type;

import lombok.Getter;

@Getter
public class AuthInfo {

  private final String email;
  private final MemberRole role;
  private final Long memberId;

  public AuthInfo(String email, MemberRole role, Long memberId) {
    this.email = email;
    this.role = role;
    this.memberId = memberId;
  }

  public boolean isInstructor() {
    return this.role == MemberRole.INSTRUCTOR;
  }

  public boolean isStudent() {
    return this.role == MemberRole.STUDENT;
  }
}
