package com.gatheria.mapper;

import com.gatheria.domain.MentoringSession;
import com.gatheria.domain.SessionParticipant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MentoringSessionMapper {

  @Insert("INSERT INTO mentoring_sessions (" +
      "title, mentor_name, session_date, max_participants, " +
      "current_participants, status, created_at, updated_at) " +
      "VALUES (#{title}, #{mentorName}, #{sessionDate}, #{maxParticipants}, " +
      "#{currentParticipants}, #{status}, #{createdAt}, #{updatedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertSession(MentoringSession session);

  @Select("SELECT * FROM session_participants WHERE session_id = #{sessionId} AND student_id = #{studentId}")
  SessionParticipant findBySessionAndStudent(Long sessionId, Long memberId);

  @Update("UPDATE mentoring_sessions SET " +
      "current_participants = #{currentParticipants}, " +
      "status = #{status}, " +
      "updated_at = NOW() " +
      "WHERE id = #{id}")
  void updateSession(MentoringSession session);

  @Insert("INSERT INTO session_participants " +
      "(session_id, student_id, status, registration_time, cancelled_at) " +
      "VALUES " +
      "(#{sessionId}, #{studentId}, #{status}, #{registrationTime}, #{cancelledAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertParticipant(SessionParticipant participant);

  @Update("UPDATE session_participants SET " +
      "status = #{status}, " +
      "registration_time = #{registrationTime}, " +
      "cancelled_at = #{cancelledAt} " +
      "WHERE session_id = #{sessionId} AND student_id = #{studentId}")
  void updateParticipant(SessionParticipant participant);

  @Select("SELECT * FROM mentoring_sessions WHERE id = #{sessionId}")
  MentoringSession getSession(Long sessionId);
}
