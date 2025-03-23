package com.gatheria.domain;

import com.gatheria.domain.type.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

  private Long id;
  private String username;
  private String password;
  private AdminRole role;
}