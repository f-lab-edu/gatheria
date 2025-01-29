package com.gatheria.service;

import com.gatheria.domain.Student;
import com.gatheria.dto.request.StudentRegisterRequestDto;
import com.gatheria.dto.response.StudentRegisterResponseDto;
import com.gatheria.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private final StudentMapper studentMapper;

    @Autowired
    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public StudentRegisterResponseDto register(StudentRegisterRequestDto request) {
        Student student = request.toDomain();
        studentMapper.insertStudent(student);

        return StudentRegisterResponseDto.from(student);
    }
}
