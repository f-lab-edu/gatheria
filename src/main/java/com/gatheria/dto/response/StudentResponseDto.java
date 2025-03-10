package com.gatheria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

  private Long id;
  private String name;
  private String email;

  public static StudentResponseDto of(MemberDto member) {
    return StudentResponseDto.builder()
        .id(member.getId())
        .name(member.getName())
        .email(member.getEmail())
        .build();
  }
}
