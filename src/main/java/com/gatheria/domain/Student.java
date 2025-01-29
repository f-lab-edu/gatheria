package com.gatheria.domain;

import lombok.Getter;

@Getter
public class Student {
    private Long id;
    private BaseMember baseMember;

    public Student(BaseMember baseMember) {
        this.baseMember = baseMember;
    }
}