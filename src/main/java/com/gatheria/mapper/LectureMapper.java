package com.gatheria.mapper;

import com.gatheria.domain.Lecture;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LectureMapper {

  void insertLecture(Lecture lecture);

  List<Lecture> findByInstructorId(Long memberId);

  @Select("SELECT lecture_id FROM lecture_students WHERE student_id = #{studentId}")
  List<Long> findLectureIdsByStudentId(@Param("studentId") Long studentId);

  @Select("SELECT * FROM lectures WHERE id IN (${lectureIds})")
  List<Lecture> findLecturesByIds(@Param("lectureIds") List<Long> lectureIds);

  @Select("SELECT * FROM lectures WHERE id = #{lectureId} AND code = #{lectureCode}")
  Lecture findByCodeAndId(@Param("lectureCode") String lectureCode,
      @Param("lectureId") Long lectureId);

  @Select("SELECT  * FROM lectures WHERE  code = #{code}")
  Lecture findLectureByCode(@Param("code") String code);

  @Select("SELECT EXISTS (" +
      "SELECT 1 FROM lecture_students " +
      "WHERE student_id = #{studentId} " +
      "AND lecture_id = #{lectureId}" +
      ")")
  boolean existEnrollmentByStudentIdAndLectureID(@Param("studentId") Long studentId,
      @Param("lectureId") Long lectureId);

  @Insert("INSERT INTO lecture_students (student_id, lecture_id, created_at, updated_at) " +
      "VALUES (#{studentId}, #{lectureId}, NOW(), NOW())")
  void insertLectureStudent(
      @Param("studentId") Long studentId,
      @Param("lectureId") Long lectureId
  );


}
