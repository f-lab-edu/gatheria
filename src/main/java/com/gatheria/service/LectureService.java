package com.gatheria.service;

import com.gatheria.domain.Lecture;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MemberRole;
import com.gatheria.dto.request.LectureCreateRequestDto;

import com.gatheria.dto.response.LectureResponseDto;
import com.gatheria.mapper.LectureMapper;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LectureService {

    private final LectureMapper lectureMapper;

    public LectureService(LectureMapper lectureMapper) {
        this.lectureMapper = lectureMapper;
    }

    @Transactional
    public void createLecture(LectureCreateRequestDto request, AuthInfo authInfo) {
        if(authInfo.getRole() != MemberRole.INSTRUCTOR) {
            throw new RuntimeException();
        }
        Lecture lecture = Lecture.of(request.getName(), authInfo.getMemberId());
        lectureMapper.insertLecture(lecture);
    }

    public List<LectureResponseDto> getLecturesByInstructor(AuthInfo authInfo) {
        List<Lecture> lectures = lectureMapper.findByInstructorId(authInfo.getMemberId());
        return LectureResponseDto.from(lectures);
    }
}
