package com.gatheria.dto.request;


import com.gatheria.domain.BaseMember;
import com.gatheria.domain.Instructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorRegisterRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone;
    private String affiliation;

    public BaseMember toBaseMember() {
        return new BaseMember(email,password,name,phone,false);
    }

    public Instructor toDomain() {
        BaseMember baseMember = toBaseMember();
        return new Instructor(baseMember,affiliation);
    }
}
