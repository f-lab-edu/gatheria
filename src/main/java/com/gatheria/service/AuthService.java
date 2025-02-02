package com.gatheria.service;

import com.gatheria.common.util.JwtUtil;
import com.gatheria.domain.Instructor;
import com.gatheria.domain.Student;
import com.gatheria.dto.request.LoginRequestDto;
import com.gatheria.dto.response.LoginResponseDto;
import com.gatheria.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(AuthMapper authMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponseDto authenticate(LoginRequestDto request, String role) {
        if ("student".equalsIgnoreCase(role)) {
            return authenticateStudent(request);
        } else if ("instructor".equalsIgnoreCase(role)) {
            return authenticateInstructor(request);
        } else {
            throw new RuntimeException("Invalid role");
        }
    }

    private LoginResponseDto authenticateStudent(LoginRequestDto request) {
        Student student = authMapper.findStudentByEmail(request.getEmail());
        if (student == null) {
            throw new RuntimeException("Student not found");
        }
        if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String accessToken = jwtUtil.createAccessToken(request.getEmail(),"student");

        return LoginResponseDto.from(accessToken, student);
    }

    private LoginResponseDto authenticateInstructor(LoginRequestDto request) {
        Instructor instructor = authMapper.findInstructorByEmail(request.getEmail());
        if (instructor == null) {
            throw new RuntimeException("Instructor not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), instructor.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String accessToken = jwtUtil.createAccessToken(request.getEmail(),"instructor");

        return LoginResponseDto.from(accessToken, instructor);
    }
}
