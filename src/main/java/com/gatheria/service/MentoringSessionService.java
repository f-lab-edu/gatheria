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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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
    log.info("세션 등록 시작: sessionId={}, studentId={}", sessionId, authInfo.getStudentId());

    authInfo.validateStudent();
    log.debug("학생 검증 완료");

    log.info("세션 배타적 락 획득 시도: sessionId={}", sessionId);
    //MentoringSession session = mentoringSessionMapper.getSession(sessionId);
    MentoringSession session = mentoringSessionMapper.getSessionForUpdate(sessionId);
    log.info("세션 락 획득 완료: sessionId={}, 현재참여자={}", sessionId, session.getCurrentParticipants());

    // 존재하지 않는 멘토링
    if (session == null) {
      log.warn("존재하지 않는 세션: sessionId={}", sessionId);
      return MentoringSessionRegistrationResponseDto.fail("해당 세션이 존재x.", HttpStatus.NOT_FOUND);
    }

    // 이미 등록된 멘토링인지 확인
    SessionParticipant existing = mentoringSessionMapper.findLatestBySessionAndStudent(sessionId,
        authInfo.getStudentId());
    log.debug("기존 참여 조회 완료: sessionId={}, 기존참여={}", sessionId, existing != null);

    // 이미 기록 존재 & 등록된 상태
    if (existing != null && SessionParticipantStatus.REGISTERED == existing.getStatus()) {
      log.warn("이미 등록된 세션: sessionId={}, studentId={}", sessionId, authInfo.getStudentId());
      return MentoringSessionRegistrationResponseDto.fail("이미 등록된 세션입니다.", HttpStatus.CONFLICT);
    }

    // 현재 정원 초과인 상태인지 체크
    if (session.getCurrentParticipants() >= session.getMaxParticipants()) {
      log.warn("정원 초과: sessionId={}, 현재={}, 최대={}",
          sessionId, session.getCurrentParticipants(), session.getMaxParticipants());

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
      log.warn("등록 불가 상태: sessionId={}, 현재상태={}", sessionId, session.getStatus());
      return MentoringSessionRegistrationResponseDto.fail(
          "현재 등록 불가 , 세션 상태: " + session.getStatus(),
          HttpStatus.BAD_REQUEST
      );
    }

    SessionParticipant participant = SessionParticipant.of(sessionId, authInfo.getStudentId(),
        requestAt);
    participant.completeRegistration();
    //===Deadlock 발생 가능성이 있음===
//    mentoringSessionMapper.insertParticipant(participant);
//    session.incrementCurrentParticipants(); //
//    mentoringSessionMapper.updateSession(session);

    // 순서 변경 (update -> insert)
    log.info("세션 업데이트 시작: sessionId={}, 현재참여자={}", sessionId, session.getCurrentParticipants());
    session.incrementCurrentParticipants();
    mentoringSessionMapper.updateSession(session);
    log.info("세션 업데이트 완료: sessionId={}, 업데이트된참여자={}", sessionId, session.getCurrentParticipants());

    log.info("참여자 등록 시작: sessionId={}, studentId={}", sessionId, authInfo.getStudentId());
    mentoringSessionMapper.insertParticipant(participant);
    log.info("참여자 등록 완료: sessionId={}, participantId={}", sessionId, participant.getId());

    log.info("세션 등록 완료: sessionId={}", sessionId);
    //===Deadlock 발생 가능성이 있음===

    log.info("트랜잭션 완료 직전: sessionId={}", sessionId);
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