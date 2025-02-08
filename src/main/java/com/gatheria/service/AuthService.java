package com.gatheria.service;

import com.gatheria.common.util.JwtUtil;
import com.gatheria.domain.Instructor;
import com.gatheria.domain.Member;
import com.gatheria.domain.Student;
import com.gatheria.domain.type.MemberRole;
import com.gatheria.dto.request.LoginRequestDto;
import com.gatheria.dto.response.LoginResponseDto;
import com.gatheria.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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

    public LoginResponseDto authenticate(LoginRequestDto request, String roleStr) {

        MemberRole role = MemberRole.fromString(roleStr);

        Member member = switch (role) {
            case STUDENT -> authMapper.findStudentByEmail(request.getEmail());
            case INSTRUCTOR -> authMapper.findInstructorByEmail(request.getEmail());
        };

        if (member == null) {
            throw new RuntimeException("Member not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String accessToken = jwtUtil.createAccessToken(request.getEmail(), role.getValue());

        return LoginResponseDto.from(accessToken, member);
    }
}