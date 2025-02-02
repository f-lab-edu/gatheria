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

    public Instructor toDomain() {
        return Instructor.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .affiliation(affiliation)
                .active(false)
                .build();
    }
}
