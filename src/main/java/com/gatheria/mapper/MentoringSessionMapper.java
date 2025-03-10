package com.gatheria.mapper;

import com.gatheria.domain.MentoringSession;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface MentoringSessionMapper {

  @Insert("INSERT INTO mentoring_sessions (" +
      "title, mentor_name, session_date, max_participants, " +
      "current_participants, status, created_at, updated_at) " +
      "VALUES (#{title}, #{mentorName}, #{sessionDate}, #{maxParticipants}, " +
      "#{currentParticipants}, #{status}, #{createdAt}, #{updatedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertSession(MentoringSession session);
}
