package com.gatheria.mapper;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {

  Student findStudentByEmail(String email);

  Instructor findInstructorByEmail(String email);

  Long findStudentIdByMemberId(@Param("memberId") Long memberId);

  Long findInstructorIdByMemberId(@Param("memberId") Long memberId);
}
