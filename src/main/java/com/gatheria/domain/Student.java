package com.gatheria.domain;

import com.gatheria.domain.type.MemberRole;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Student extends Member{

    public Student(String email, String password, String name, String phone) {
        super(email, password, name, phone, false);
    }

    public static Student of(String email, String password, String name, String phone){
        return new Student(email,password,name,phone);
    }

    @Override
    public String getAffiliation() {
        return null;
    }

    @Override
    public MemberRole getRole() {
        return MemberRole.STUDENT;
    }
}