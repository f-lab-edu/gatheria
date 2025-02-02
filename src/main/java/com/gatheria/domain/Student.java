package com.gatheria.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Student {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private boolean active;

    public void setActive() {
        this.active = true;
    }
}