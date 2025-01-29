package com.gatheria.mapper;

import com.gatheria.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StudentMapper {
    void insertStudent(Student student);
}
