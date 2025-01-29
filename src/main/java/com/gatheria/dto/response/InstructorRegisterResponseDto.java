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

    public static InstructorRegisterResponseDto from(Instructor instructor) {
        return new InstructorRegisterResponseDto(
                instructor.getEmail(),
                instructor.getName(),
                instructor.getPhone(),
                instructor.getAffiliation(),
                instructor.isActive()
        );
    }
}
