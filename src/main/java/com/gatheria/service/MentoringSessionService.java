package com.gatheria.service;

import com.gatheria.domain.MentoringSession;
import com.gatheria.domain.SessionParticipant;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.domain.type.MentoringStatus;
import com.gatheria.domain.type.SessionParticipantStatus;
import com.gatheria.dto.request.MentoringSessionCreateRequestDto;
import com.gatheria.dto.response.MentoringSessionCreateResponseDto;
import com.gatheria.dto.response.MentoringSessionRegistrationResponseDto;
import com.gatheria.dto.response.MentoringSessionResponseDto;
import com.gatheria.mapper.MentoringSessionMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MentoringSessionService {

  private final MentoringSessionMapper mentoringSessionMapper;

  @Autowired
  public MentoringSessionService(MentoringSessionMapper mentoringSessionMapper) {
    this.mentoringSessionMapper = mentoringSessionMapper;
  }

  public MentoringSessionCreateResponseDto createMentoringSession(
      MentoringSessionCreateRequestDto request) {

    MentoringSession session = MentoringSession.of(
        request.getTitle(),
        request.getMentorName(),
        request.getSessionDate(),
        request.getWaitingStartDate(),
        request.getWaitingEndDate(),
        request.getMaxParticipants()
    );

    mentoringSessionMapper.insertSession(session);

    return MentoringSessionCreateResponseDto.of(session);
  }

  @Transactional
  public MentoringSessionRegistrationResponseDto registerSession(Long sessionId,
      AuthInfo authInfo, LocalDateTime requestAt) {
    // TODO : AuthInfo 수정 PR 반영 이후 수정 필요
    if (!authInfo.isStudent()) {
      throw new RuntimeException("학생만 가능함");
    }

    MentoringSession session = mentoringSessionMapper.getSession(sessionId);

    // 존재하지 않는 멘토링
    if (session == null) {
      return MentoringSessionRegistrationResponseDto.fail("해당 세션이 존재x.", HttpStatus.NOT_FOUND);
    }

    // 이미 등록된 멘토링인지 확인
    SessionParticipant existing = mentoringSessionMapper.findLatestBySessionAndStudent(sessionId,
        authInfo.getStudentId());

    // 이미 기록 존재 & 등록된 상태
    if (existing != null && SessionParticipantStatus.REGISTERED == existing.getStatus()) {
      return MentoringSessionRegistrationResponseDto.fail("이미 등록된 세션입니다.", HttpStatus.CONFLICT);
    }

    // 현재 정원 초과인 상태인지 체크
    if (session.getCurrentParticipants() >= session.getMaxParticipants()) {
      SessionParticipant participant = SessionParticipant.of(sessionId, authInfo.getStudentId(),
          requestAt);
      participant.reject();
      mentoringSessionMapper.insertParticipant(participant);
      return MentoringSessionRegistrationResponseDto.rejected(
          "정원 초과",
          HttpStatus.CONFLICT,
          sessionId,
          participant.getId()
      );
    }

    if (session.getStatus() != MentoringStatus.WAITING_OPEN) {
      return MentoringSessionRegistrationResponseDto.fail(
          "현재 등록 불가 , 세션 상태: " + session.getStatus(),
          HttpStatus.BAD_REQUEST
      );
    }

    SessionParticipant participant = SessionParticipant.of(sessionId, authInfo.getStudentId(),
        requestAt);
    participant.completeRegistration();
    mentoringSessionMapper.insertParticipant(participant);

    session.incrementCurrentParticipants();
    mentoringSessionMapper.updateSession(session);

    return MentoringSessionRegistrationResponseDto.success(
        session.getId(),
        session.getTitle(),
        session.getSessionDate(),
        participant.getId()
    );
  }

  public List<MentoringSessionResponseDto> getAllSessions() {
    List<MentoringSession> sessions = mentoringSessionMapper.findAllSessions();
    return MentoringSessionResponseDto.listOf(sessions);
  }


  public MentoringSessionResponseDto getSession(Long sessionId) {
    MentoringSession session = mentoringSessionMapper.getSession(sessionId);
    return MentoringSessionResponseDto.of(session);
  }

  public List<MentoringSessionResponseDto> getMySessions(AuthInfo authInfo) {
    List<MentoringSession> sessions = mentoringSessionMapper.findSessionsByStudentId(
        authInfo.getStudentId());
    return MentoringSessionResponseDto.listOf(sessions);
  }
}