package com.gatheria.mapper;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    Student findStudentByEmail(String email);
    Instructor findInstructorByEmail(String email);
}
