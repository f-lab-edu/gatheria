package com.gatheria.controller;

import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.response.TeamResponseDto;
import com.gatheria.service.TeamService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lectures/{lectureId}/teams")
public class TeamController {


  private final TeamService teamService;

  @Autowired
  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  /**
   * 팀 자동 배정
   */
  @PostMapping("/auto-assign")
  public ResponseEntity<Void> autoAssignTeams(
      @PathVariable Long lectureId,
      @RequestParam int teamSize,
      @RequestAttribute("authInfo") AuthInfo authInfo) {

    teamService.autoAssignTeams(lectureId, teamSize, authInfo);
    return ResponseEntity.ok().build();
  }

  /**
   * 팀 수동 배정
   */
  @PostMapping("/{teamId}/assign")
  public ResponseEntity<Void> manualAssignTeams(
      @PathVariable Long lectureId,
      @PathVariable Long teamId,
      @RequestParam Long studentId,
      @RequestAttribute("authInfo") AuthInfo authInfo
  ) {
    teamService.manualAssignTeams(lectureId, teamId, studentId, authInfo);
    return ResponseEntity.ok().build();
  }

  /**
   * 팀 목록 조회 (학생도 가능해야함) (Team Member에 중복 제한 조건 걸려잇음 중복 검증 일단 x)
   */
  @GetMapping
  public ResponseEntity<List<TeamResponseDto>> findTeamsByLecture(
      @PathVariable Long lectureId,
      @RequestAttribute("authInfo") AuthInfo authInfo
  ) {
    List<TeamResponseDto> response = teamService.getTeamsByLectureId(lectureId, authInfo);

    return ResponseEntity.ok(response);
  }

  /**
   * 특정 팀 조회
   */
  @GetMapping("/{teamId}")
  public ResponseEntity<TeamResponseDto> showTeam(
      @PathVariable Long lectureId,
      @PathVariable Long teamId,
      @RequestAttribute("authInfo") AuthInfo authInfo
  ) {
    TeamResponseDto response = teamService.getTeamByTeamId(lectureId, teamId, authInfo);

    return ResponseEntity.ok(response);
  }

}
