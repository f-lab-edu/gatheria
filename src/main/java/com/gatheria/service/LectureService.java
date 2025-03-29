package com.gatheria.service;

import com.gatheria.domain.Lecture;
import com.gatheria.domain.Student;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.LectureCreateRequestDto;
import com.gatheria.dto.response.LectureJoinResponseDto;
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
    authInfo.validateInstructor();

    Lecture lecture = Lecture.of(request.getName(), authInfo.getInstructorId(),
        request.getClassSize());
    lectureMapper.insertLecture(lecture);
  }

  public List<LectureResponseDto> getLectureListByInstructorId(AuthInfo authInfo) {
    authInfo.validateInstructor();

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
    authInfo.validateStudent();

    Long studentId = authInfo.getStudentId();

    List<Long> lectureIds = lectureMapper.findLectureIdsByStudentId(studentId);

    if (lectureIds.isEmpty()) {
      return Collections.emptyList();
    }

    List<Lecture> lectures = lectureMapper.findLecturesByIds(lectureIds);

    return LectureResponseDto.from(lectures);
  }

  public LectureResponseDto findLectureByCodeAndId(Long lectureId,
      AuthInfo authInfo) {
    Lecture lecture = lectureMapper.findLectureById(lectureId);

    if (lecture == null) {
      throw new RuntimeException();
    }

    if (authInfo.isInstructor()) {
      if (!lecture.isOwnedBy(authInfo.getInstructorId())) {
        throw new RuntimeException("본인의 강의가 아닙니다");
      }
    } else if (authInfo.isStudent()) {
      if (!lectureMapper.existEnrollmentByStudentIdAndLectureID(authInfo.getStudentId(),
          lectureId)) {
        throw new RuntimeException("수강 신청한 강의가 아닙니다");
      }
    } else {
      throw new RuntimeException("접근 권한이 없습니다");
    }

    return LectureResponseDto.of(lecture);
  }

  @Transactional
  public LectureJoinResponseDto joinLecture(String code, AuthInfo authInfo) {

    authInfo.validateStudent();

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

    return LectureJoinResponseDto.from(lecture);
  }
}
