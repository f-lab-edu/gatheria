package com.gatheria.service;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Student;
import com.gatheria.dto.request.InstructorRegisterRequestDto;
import com.gatheria.dto.request.StudentRegisterRequestDto;
import com.gatheria.dto.response.InstructorRegisterResponseDto;
import com.gatheria.dto.response.PagedInstructorResponseDto;
import com.gatheria.dto.response.StudentRegisterResponseDto;
import com.gatheria.mapper.MemberMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public InstructorRegisterResponseDto register(InstructorRegisterRequestDto request) {
        Instructor instructor = request.toDomain();

        String encodedPassword = passwordEncoder.encode(instructor.getPassword());

        instructor = Instructor.builder()
                .id(instructor.getId())
                .email(instructor.getEmail())
                .password(encodedPassword)
                .name(instructor.getName())
                .phone(instructor.getPhone())
                .affiliation(instructor.getAffiliation())
                .active(false)
                .build();

        memberMapper.insertInstructor(instructor);

        return InstructorRegisterResponseDto.from(instructor);
    }

    public StudentRegisterResponseDto register(StudentRegisterRequestDto request) {
        Student student = request.toDomain();

        String encodedPassword = passwordEncoder.encode(student.getPassword());

        student = Student.builder()
                .id(student.getId())
                .email(student.getEmail())
                .password(encodedPassword)
                .name(student.getName())
                .phone(student.getPhone())
                .active(false)
                .build();

        memberMapper.insertStudent(student);

        return StudentRegisterResponseDto.from(student);
    }


    public boolean emailExists(String email, String role) {
        if ("student".equalsIgnoreCase(role)) {
            return memberMapper.existsByEmailInStudents(email);
        } else if ("instructor".equalsIgnoreCase(role)) {
            System.out.println(memberMapper.existsByEmailInInstructors(email));
            return memberMapper.existsByEmailInInstructors(email);
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }

    public PagedInstructorResponseDto getPendingInstructors(int page, int size) {
        int offset = (page - 1) * size;
        List<Instructor> pendingInstructors = memberMapper.findPendingInstructors(offset, size);
        int totalCount = memberMapper.countPendingInstructors();

        return PagedInstructorResponseDto.from(pendingInstructors, totalCount, page, size);
    }

    public void approveInstructor(Long id) {
        Instructor instructor = memberMapper.findInstructorByID(id);
        instructor.setActive();
        memberMapper.updateInstructorActivateStatus(id, instructor.isActive());
    }
}
