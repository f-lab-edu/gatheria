package com.gatheria.domain;

import com.gatheria.domain.type.MemberRole;
import lombok.Getter;


@Getter
public class Instructor extends Member{
    private final String affiliation;

    public Instructor(String email, String password, String name, String phone, String affiliation) {
        super(email, password, name, phone, false);
        this.affiliation = affiliation;
    }

    public static Instructor of(String email, String password, String name, String phone, String affiliation) {
        return new Instructor(email, password, name, phone, affiliation);
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    @Override
    public MemberRole getRole() {
        return MemberRole.INSTRUCTOR;
    }
}