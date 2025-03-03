package com.gatheria.service;

import com.gatheria.domain.Lecture;
import com.gatheria.domain.Student;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MemberRole;
import com.gatheria.dto.request.LectureCreateRequestDto;
import com.gatheria.dto.response.LectureJoinResponse;
import com.gatheria.dto.response.LectureResponseDto;
import com.gatheria.dto.response.StudentResponseDto;
import com.gatheria.mapper.LectureMapper;
import com.gatheria.mapper.MemberMapper;
import java.util.Collections;
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

  public List<LectureResponseDto> getLectureListByInstructorID(AuthInfo authInfo) {
    List<Lecture> lectures = lectureMapper.findByInstructorId(authInfo.getMemberId());
    return LectureResponseDto.from(lectures);
  }

  public List<LectureResponseDto> getLectureListByStudentId(AuthInfo authInfo) {
    Long studentId = authInfo.getMemberId();

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

  public List<StudentResponseDto> getStudentsByLectureId(Long lectureId, AuthInfo authInfo) {

    validateLectureAccess(lectureId, authInfo);

    List<StudentResponseDto> students = lectureMapper.findStudentsByLectureId(lectureId);

    return students;
  }

  private void validateLectureAccess(long lectureId, AuthInfo authInfo) {
    Lecture lecture = lectureMapper.findLectureById(lectureId);

    if (lecture == null) {
      throw new IllegalArgumentException("Invalid Lecture");
    }

    if (authInfo.getRole() == MemberRole.INSTRUCTOR) {
      if (!Objects.equals(lecture.getInstructorId(), authInfo.getMemberId())) {
        throw new IllegalArgumentException("교수자는 해당 수업에 접근 권한 x");
      }
    } else if (authInfo.getRole() == MemberRole.STUDENT) {
      boolean isEnrolled = lectureMapper.existEnrollmentByStudentIdAndLectureID(
          authInfo.getMemberId(), lectureId);
      if (!isEnrolled) {
        throw new IllegalArgumentException("학생이 해당 수업에 등록 x");
      }

    } else {
      throw new IllegalArgumentException("Invalid Role");
    }
  }
}
