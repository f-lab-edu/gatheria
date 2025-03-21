package com.gatheria.mapper;

import com.gatheria.domain.Lecture;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LectureMapper {

  boolean existEnrollmentByStudentIdAndLectureID(Long studentId, Long lectureId);

  List<Long> findStudentIdsByLectureId(Long lectureId);

  Lecture findLectureById(long lectureId);

  void insertLecture(Lecture lecture);

  List<Lecture> findByInstructorId(Long instructorId);

  List<Long> findLectureIdsByStudentId(Long studentId);

  List<Lecture> findLecturesByIds(List<Long> lectureIds);

  Lecture findLectureByCode(String code);


  void insertLectureStudent(Long id, Long id1);
}
