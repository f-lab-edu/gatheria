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
import java.util.Collections;
import java.util.List;
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
    Lecture lecture = Lecture.of(request.getName(), authInfo.getInstructorId(),
        request.getClassSize());
    lectureMapper.insertLecture(lecture);
  }

  public List<LectureResponseDto> getLectureListByInstructorId(AuthInfo authInfo) {
    if (!authInfo.isInstructor()) {
      throw new RuntimeException("교수자 권한이 필요");
    }

    Long instructorId = authInfo.getInstructorId();
    if (instructorId == null) {
      throw new RuntimeException("교수자 ID를 찾지 못함");
    }

    List<Lecture> lectures = lectureMapper.findByInstructorId(instructorId);

    if (lectures.isEmpty()) {
      return Collections.emptyList();
    }

    return LectureResponseDto.from(lectures);
  }

  public List<LectureResponseDto> getLectureListByStudentId(AuthInfo authInfo) {
    if (!authInfo.isStudent()) {
      throw new RuntimeException("학생 권한이 필요");
    }

    Long studentId = authInfo.getStudentId();

    List<Long> lectureIds = lectureMapper.findLectureIdsByStudentId(studentId);

    if (lectureIds.isEmpty()) {
      return Collections.emptyList();
    }

    List<Lecture> lectures = lectureMapper.findLecturesByIds(lectureIds);

    return LectureResponseDto.from(lectures);
  }

  public LectureResponseDto findLectureByCodeAndId(String lectureCode, Long lectureId,
      AuthInfo authInfo) {
    Lecture lecture = lectureMapper.findByCodeAndId(lectureCode, lectureId);

    if (lecture == null) {
      throw new RuntimeException();
    }
    if (authInfo.isInstructor()
        && lecture.isOwnedBy(authInfo.getInstructorId())) {
      throw new RuntimeException();
    }// TODO: 학생 -> 수강신청한 강의인지 확인하는 로직 추가 필요

    return LectureResponseDto.of(lecture);
  }

  @Transactional
  public LectureJoinResponse joinLecture(String code, AuthInfo authInfo) {
    if (!authInfo.isStudent()) {
      throw new RuntimeException("학생만 강의에 참여 가능");
    }

    Student student = memberMapper.findStudentById(authInfo.getStudentId());

    if (student == null) {
      throw new RuntimeException("학생 정보를 찾을 수 없음");
    }

    Lecture lecture = lectureMapper.findLectureByCode(code);

    if (lecture == null) {
      throw new RuntimeException("강의를 찾을 수 없음");
    }

    if (lectureMapper.existEnrollmentByStudentIdAndLectureID(student.getId(), lecture.getId())) {
      throw new RuntimeException("이미 수강신청한 강의");
    }

    lectureMapper.insertLectureStudent(student.getId(), lecture.getId());

    return LectureJoinResponse.from(lecture);
  }


}
