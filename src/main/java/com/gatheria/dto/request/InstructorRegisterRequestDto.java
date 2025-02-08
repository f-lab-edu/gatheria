package com.gatheria.dto.request;


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

    public Instructor toDomain(String encodedPassword) {
        return Instructor.of(email, encodedPassword, name, phone, affiliation);
    }
}
