package com.gatheria.dto.request;

import com.gatheria.domain.Admin;
import com.gatheria.domain.type.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterRequestDto {

  private String username;
  private String password;
  private String role;

  public Admin toDomain(String encodedPassword) {
    return new Admin(null, this.username, encodedPassword, AdminRole.valueOf(this.role));
  }
}