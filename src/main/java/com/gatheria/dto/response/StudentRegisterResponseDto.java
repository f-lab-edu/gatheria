package com.gatheria.dto.response;


import com.gatheria.domain.Student;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StudentRegisterResponseDto {
    private final String email;
    private final String name;
    private final String phone;
    private final boolean isActive;

    public static StudentRegisterResponseDto from(Student student) {
        return new StudentRegisterResponseDto(
                student.getEmail(),
                student.getName(),
                student.getPhone(),
                student.isActive()
        );
    }
}
