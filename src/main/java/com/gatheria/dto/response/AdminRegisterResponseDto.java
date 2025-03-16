package com.gatheria.dto.response;

import com.gatheria.domain.Admin;
import com.gatheria.domain.type.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminRegisterResponseDto {

  private String username;
  private AdminRole role;

  public static AdminRegisterResponseDto from(Admin admin) {
    return new AdminRegisterResponseDto(admin.getUsername(), admin.getRole());
  }
}