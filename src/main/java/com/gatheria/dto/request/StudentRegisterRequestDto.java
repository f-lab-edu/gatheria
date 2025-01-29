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

    public Student toDomain() {
        return Student.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .isActive(false)
                .build();
    }
}
