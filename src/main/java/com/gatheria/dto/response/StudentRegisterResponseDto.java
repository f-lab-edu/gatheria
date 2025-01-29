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

    public StudentRegisterResponseDto(String email, String name, String phone, boolean isActive) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.isActive = isActive;
    }

    public static StudentRegisterResponseDto from(Student student) {
        return new StudentRegisterResponseDto(
                student.getBaseMember().getEmail(),
                student.getBaseMember().getName(),
                student.getBaseMember().getPhone(),
                student.getBaseMember().isActive()
        );
    }
}
