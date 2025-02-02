package com.gatheria.mapper;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Student;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    void insertInstructor(Instructor instructor);

    void insertStudent(Student student);

    boolean existsByEmailInStudents(String email);

    boolean existsByEmailInInstructors(String email);

    int countPendingInstructors();

    List<Instructor> findPendingInstructors(int offset, int size);

    Instructor findInstructorByID(Long id);

    void updateInstructorActivateStatus(Long id, boolean activate);
}
