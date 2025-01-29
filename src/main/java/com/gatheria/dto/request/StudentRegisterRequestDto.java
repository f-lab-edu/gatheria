package com.gatheria.dto.request;

import com.gatheria.domain.BaseMember;
import com.gatheria.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegisterRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone;

    public BaseMember toBaseMember() {
        return new BaseMember(email,password,name,phone,false);
    }

    public Student toDomain() {
        BaseMember baseMember = toBaseMember();
        return new Student(baseMember);
    }
}
