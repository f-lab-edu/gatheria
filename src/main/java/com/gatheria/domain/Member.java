package com.gatheria.domain;

import com.gatheria.domain.type.MemberRole;
import lombok.Getter;

@Getter
public abstract class Member {

  private Long id;
  private final String email;
  private String password;
  private String name;
  private String phone;
  private boolean active;

  protected Member(String email, String password, String name, String phone, boolean active) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.phone = phone;
    this.active = false;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setActive() {
    this.active = true;
  }

  void setId(Long id) {
    this.id = id;
  }

  public abstract String getAffiliation();

  public abstract MemberRole getRole();
}
