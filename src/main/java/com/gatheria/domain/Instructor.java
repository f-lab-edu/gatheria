package com.gatheria.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Instructor {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String affiliation;
    private boolean isActive;
}