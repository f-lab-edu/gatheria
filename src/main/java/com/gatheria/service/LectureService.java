package com.gatheria.service;

import com.gatheria.domain.Lecture;
import com.gatheria.domain.Student;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MemberRole;
import com.gatheria.dto.request.LectureCreateRequestDto;
import com.gatheria.dto.response.LectureJoinResponse;
import com.gatheria.dto.response.LectureResponseDto;
import com.gatheria.mapper.LectureMapper;
import com.gatheria.mapper.MemberMapper;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LectureService {

  private final LectureMapper lectureMapper;
  private final MemberMapper memberMapper;

  public LectureService(LectureMapper lectureMapper, MemberMapper memberMapper) {
    this.lectureMapper = lectureMapper;
    this.memberMapper = memberMapper;
  }

  @Transactional
  public void createLecture(LectureCreateRequestDto request, AuthInfo authInfo) {
    if (authInfo.getRole() != MemberRole.INSTRUCTOR) {
      throw new RuntimeException();
    }
    Lecture lecture = Lecture.of(request.getName(), authInfo.getMemberId());
    lectureMapper.insertLecture(lecture);
  }

  public List<LectureResponseDto> getLecturesByInstructor(AuthInfo authInfo) {
    List<Lecture> lectures = lectureMapper.findByInstructorId(authInfo.getMemberId());
    return LectureResponseDto.from(lectures);
  }

  public List<LectureResponseDto> getLecturesByStudentId(AuthInfo authInfo) {
    List<Lecture> lectures = lectureMapper.findByStudentId(authInfo.getMemberId());
    return LectureResponseDto.from(lectures);
  }

  public LectureResponseDto findByCodeAndId(String lectureCode, Long lectureId, AuthInfo authInfo) {
    Lecture lecture = lectureMapper.findByCodeAndId(lectureCode, lectureId);

    if (lecture == null) {
      throw new RuntimeException();
    }
    if (authInfo.getRole() == MemberRole.INSTRUCTOR
        && !Objects.equals(lecture.getInstructorId(), authInfo.getMemberId())) {
      throw new RuntimeException();
    }// TODO: 학생 -> 수강신청한 강의인지 확인하는 로직 추가 필요

    return LectureResponseDto.of(lecture);
  }

  @Transactional
  public LectureJoinResponse joinLecture(String code, AuthInfo authInfo) {

    Student student = memberMapper.findStudentById(authInfo.getMemberId());

    if (student == null) {
      throw new RuntimeException();
    }

    Lecture lecture = lectureMapper.findLectureByCode(code);

    if (lecture == null) {
      throw new RuntimeException();
    }

    if (lectureMapper.existEnrollmentByStudentIdAndLectureID(student.getId(), lecture.getId())) {
      throw new RuntimeException();
    }

    lectureMapper.insertLectureStudent(student.getId(), lecture.getId());

    return LectureJoinResponse.from(lecture);
  }


}
