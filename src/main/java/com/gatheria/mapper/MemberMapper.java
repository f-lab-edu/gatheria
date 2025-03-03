package com.gatheria.mapper;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Member;
import com.gatheria.domain.Student;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {

  void insertMember(Member member);

  boolean existsByEmail(String email);

  void insertInstructor(Instructor instructor);

  int countPendingInstructors();

  List<Instructor> findPendingInstructors(int offset, int size);

  Instructor findInstructorByID(Long id);

  void updateInstructorActivateStatus(Long id, boolean activate);

  void insertStudent(Student student);

  @Select("SELECT * FROM students WHERE id = #{memberId}")
  Student findStudentById(Long memberId);

  List<Member> findStudentsByIds(@Param("studentIds") List<Long> studentIds);

  // 헤갈리지말기
  @Select("SELECT student_id FROM lecture_students WHERE lecture_id = #{lectureId}")
  List<Long> findStudentIdsByLectureId(Long lectureId);

  List<Long> findMemberIdsByStudentIds(@Param("studentIds") List<Long> studentIds);

  List<Member> findMembersByIds(@Param("memberIds") List<Long> memberIds);
}
