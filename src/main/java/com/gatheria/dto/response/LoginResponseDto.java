package com.gatheria.dto.response;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private String role;
    private String affiliation;
    private String email;
    private String name;
    private boolean activate;
    private String phone;

    public static LoginResponseDto from(String accessToken, Instructor instructor) {
        return new LoginResponseDto(
                accessToken,
                "instructor",
                instructor.getAffiliation(),
                instructor.getEmail(),
                instructor.getName(),
                instructor.isActive(),
                instructor.getPhone()
        );
    }

    public static LoginResponseDto from(String accessToken, Student student) {
        return new LoginResponseDto(
                accessToken,
                "student",
                null,
                student.getEmail(),
                student.getName(),
                student.isActive(),
                student.getPhone()
        );
    }
}
