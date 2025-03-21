package com.gatheria.mapper;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Member;
import com.gatheria.domain.Student;
import com.gatheria.dto.response.StudentInfoResponseDto;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
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

  @Select("SELECT * FROM students WHERE id = #{studentId}")
  Student findStudentById(Long studentId);


  //---TeamService.java ---
  @MapKey("studentId")
  Map<Long, StudentInfoResponseDto> findStudentInfoMapByIds(
      @Param("studentIds") List<Long> studentIds);
}
