package com.gatheria.service;

import com.gatheria.domain.MentoringSession;
import com.gatheria.domain.SessionParticipant;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.request.MentoringSessionCreateRequestDto;
import com.gatheria.dto.response.MentoringSessionCreateResponseDto;
import com.gatheria.dto.response.SessionRegistrationResponseDto;
import com.gatheria.mapper.MentoringSessionMapper;
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
        request.getMaxParticipants()
    );

    mentoringSessionMapper.insertSession(session);

    return MentoringSessionCreateResponseDto.of(session);
  }

  @Transactional
  public SessionRegistrationResponseDto registerSession(Long sessionId, AuthInfo authInfo) {

    if (!authInfo.isStudent()) {
      throw new RuntimeException("학생만 가능함");
    }

    MentoringSession session = mentoringSessionMapper.getSession(sessionId);

    if (session == null) {
      return SessionRegistrationResponseDto.fail("해당 세션이 존재x.", HttpStatus.NOT_FOUND);
    }

    SessionParticipant existing = mentoringSessionMapper.findBySessionAndStudent(sessionId,
        authInfo.getMemberId());

    if (existing != null && "REGISTERED".equals(existing.getStatus())) {
      return SessionRegistrationResponseDto.fail("이미 등록된 세션입니다.", HttpStatus.CONFLICT);
    }

    if (!"OPEN".equals(session.getStatus())) {
      return SessionRegistrationResponseDto.fail(
          "현재 등록 불가 , 세션 상태: " + session.getStatus(),
          HttpStatus.BAD_REQUEST
      );
    }

    if (session.getCurrentParticipants() >= session.getMaxParticipants()) {
      return SessionRegistrationResponseDto.fail("정원 초과", HttpStatus.CONFLICT);
    }

    session.incrementCurrentParticipants();
    mentoringSessionMapper.updateSession(session);

    if (existing != null) {
      SessionParticipant.reRegister(existing);
      mentoringSessionMapper.updateParticipant(existing);
    } else {
      SessionParticipant participant = SessionParticipant.of(sessionId, authInfo.getMemberId());
      mentoringSessionMapper.insertParticipant(participant);
    }

    return SessionRegistrationResponseDto.success(
        session.getId(),
        session.getTitle(),
        session.getSessionDate()
    );
  }
}