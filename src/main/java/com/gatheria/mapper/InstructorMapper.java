package com.gatheria.mapper;

import com.gatheria.domain.Instructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InstructorMapper {
    void insertInstructor(Instructor instructor);
}
