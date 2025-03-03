package com.gatheria.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

  private Long id;
  private String email;
  private String password;
  private String name;
  private String phone;
}