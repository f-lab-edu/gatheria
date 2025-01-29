package com.gatheria.service;

import com.gatheria.domain.Instructor;
import com.gatheria.dto.request.InstructorRegisterRequestDto;
import com.gatheria.dto.response.InstructorRegisterResponseDto;
import com.gatheria.mapper.InstructorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstructorService {
    private final InstructorMapper instructorMapper;

    @Autowired
    public InstructorService(InstructorMapper instructorMapper) {
        this.instructorMapper = instructorMapper;
    }

    public InstructorRegisterResponseDto register(InstructorRegisterRequestDto request) {
        Instructor instructor = request.toDomain();
        instructorMapper.insertInstructor(instructor);

        return InstructorRegisterResponseDto.from(instructor);
    }
}
