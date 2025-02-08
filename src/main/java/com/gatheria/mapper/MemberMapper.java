package com.gatheria.mapper;

import com.gatheria.domain.Instructor;
import com.gatheria.domain.Member;
import com.gatheria.domain.Student;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

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


}
