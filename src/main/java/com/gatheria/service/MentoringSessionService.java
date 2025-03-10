package com.gatheria.service;

import com.gatheria.domain.MentoringSession;
import com.gatheria.dto.request.MentoringSessionCreateRequestDto;
import com.gatheria.dto.response.MentoringSessionCreateResponseDto;
import com.gatheria.mapper.MentoringSessionMapper;
import org.springframework.stereotype.Service;

@Service
public class MentoringSessionService {

  private final MentoringSessionMapper mentoringSessionMapper;

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
}
