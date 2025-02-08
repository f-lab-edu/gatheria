package com.gatheria.dto.request;

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

    public Student toDomain(String encodedPassword) {
        return Student.of(email, encodedPassword, name, phone);
    }
}
