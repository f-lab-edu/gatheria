package com.gatheria.mapper;

import com.gatheria.domain.Lecture;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LectureMapper {
    void insertLecture(Lecture lecture);

    List<Lecture> findByInstructorId(Long memberId);
}
