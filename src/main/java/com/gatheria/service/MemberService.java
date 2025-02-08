package com.gatheria.service;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Student;
import com.gatheria.domain.type.MemberRole;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public InstructorRegisterResponseDto register(InstructorRegisterRequestDto request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Instructor instructor = request.toDomain(encodedPassword);

        memberMapper.insertMember(instructor);
        memberMapper.insertInstructor(instructor );

        return InstructorRegisterResponseDto.from(instructor);
    }

    @Transactional
    public StudentRegisterResponseDto register(StudentRegisterRequestDto request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Student student = request.toDomain(encodedPassword);

        memberMapper.insertMember(student);
        memberMapper.insertStudent(student);

        return StudentRegisterResponseDto.from(student);
    }

    public boolean emailExists(String email) {
        return memberMapper.existsByEmail(email);
    }

    public PagedInstructorResponseDto showPendingInstructors(int page, int size) {
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
