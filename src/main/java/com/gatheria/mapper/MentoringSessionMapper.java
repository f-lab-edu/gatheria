package com.gatheria.mapper;

import com.gatheria.domain.MentoringSession;
import com.gatheria.domain.SessionParticipant;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MentoringSessionMapper {

  @Insert("INSERT INTO mentoring_sessions (" +
      "title, mentor_name, session_date, waiting_start_date, waiting_end_date, max_participants, " +
      "current_participants, status, created_at, updated_at) " +
      "VALUES (#{title}, #{mentorName}, #{sessionDate}, #{waitingStartDate}, #{waitingEndDate}, #{maxParticipants}, "
      +
      "#{currentParticipants}, #{status}, #{createdAt}, #{updatedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertSession(MentoringSession session);

  @Update("UPDATE mentoring_sessions SET " +
      "current_participants = #{currentParticipants}, " +
      "status = #{status}, " +
      "updated_at = NOW() " +
      "WHERE id = #{id}")
  void updateSession(MentoringSession session);

  @Insert("INSERT INTO session_participants " +
      "(session_id, student_id, request_at, registered_at, status, cancelled_at, rejected_at) " +
      "VALUES " +
      "(#{sessionId}, #{studentId}, #{requestAt}, #{registeredAt}, #{status}, #{cancelledAt}, #{rejectedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertParticipant(SessionParticipant participant);

  @Select("SELECT * FROM mentoring_sessions WHERE id = #{sessionId}")
  MentoringSession getSession(Long sessionId);

  // get session + 배타적 락
  @Select("SELECT * FROM mentoring_sessions WHERE id = #{sessionId} FOR UPDATE")
  MentoringSession getSessionForUpdate(Long sessionId);


  @Select("SELECT * FROM mentoring_sessions")
  List<MentoringSession> findAllSessions();

  @Select("""
          SELECT ms.*
          FROM mentoring_sessions ms
          JOIN session_participants sp ON ms.id = sp.session_id
          WHERE sp.student_id = #{studentId}
            AND sp.status = 'REGISTERED'
      """)
  List<MentoringSession> findSessionsByStudentId(Long studentId);

  @Select("SELECT * FROM session_participants WHERE session_id = #{sessionId} AND student_id = #{studentId} ORDER BY request_at DESC LIMIT 1")
  SessionParticipant findLatestBySessionAndStudent(Long sessionId, Long studentId);

  @Update("""
          UPDATE mentoring_sessions
          SET current_participants = current_participants + 1,
              updated_at = NOW()
          WHERE id = #{sessionId}
            AND current_participants < max_participants
      """)
  int incrementParticipantCountIfNotFull(@Param("sessionId") Long sessionId);
}
