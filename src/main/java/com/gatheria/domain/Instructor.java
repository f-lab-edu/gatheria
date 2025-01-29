package com.gatheria.domain;

import lombok.Getter;

@Getter
public class Instructor {
    private Long id;
    private BaseMember baseMember;
    private String affiliation;

    public Instructor(BaseMember baseMember, String affiliation) {
        this.baseMember = baseMember;
        this.affiliation = affiliation;
    }
}