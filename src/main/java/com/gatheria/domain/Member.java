package com.gatheria.domain;

import com.gatheria.domain.type.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Member {
    private Long id;
    private final String email;
    private final String password;
    @Setter
    private String name;
    @Setter
    private String phone;
    @Getter
    private boolean active;

    protected Member(String email, String password, String name, String phone, boolean active) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.active = false;
    }

    public void setActive() {
        this.active = true;
    }

    void setId(Long id) {
        this.id = id;
    }

    public abstract String getAffiliation();
    public  abstract MemberRole getRole();
}
