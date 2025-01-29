package com.gatheria.dto.response;

import com.gatheria.domain.Instructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InstructorRegisterResponseDto {
    private final String email;
    private final String name;
    private final String phone;
    private final String affiliation;
    private final boolean isActive;

    public InstructorRegisterResponseDto(String email, String name, String phone, String affiliation, boolean isActive) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.affiliation = affiliation;
        this.isActive = isActive;
    }

    public static InstructorRegisterResponseDto from (Instructor instructor) {
        return new InstructorRegisterResponseDto(
                instructor.getBaseMember().getEmail(),
                instructor.getBaseMember().getName(),
                instructor.getBaseMember().getPhone(),
                instructor.getAffiliation(),
                instructor.getBaseMember().isActive()
        );
    }
}
