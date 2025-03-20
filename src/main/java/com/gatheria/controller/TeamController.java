package com.gatheria.controller;

import com.gatheria.common.annotation.Auth;
import com.gatheria.domain.type.AuthInfo;
import com.gatheria.dto.response.TeamResponseDto;
import com.gatheria.service.TeamService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/auto-assign")
  public ResponseEntity<Void> autoAssignTeams(
      @PathVariable Long lectureId,
      @RequestParam int teamSize,
      @Auth AuthInfo authInfo) {

    teamService.autoAssignTeams(lectureId, teamSize, authInfo);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{teamId}/assign")
  public ResponseEntity<Void> manualAssignTeams(
      @PathVariable Long lectureId,
      @PathVariable Long teamId,
      @RequestParam Long studentId,
      @Auth AuthInfo authInfo
  ) {
    teamService.manualAssignTeams(lectureId, teamId, studentId, authInfo);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<TeamResponseDto>> showTeamsByLecture(
      @PathVariable Long lectureId,
      @Auth AuthInfo authInfo
  ) {
    List<TeamResponseDto> response = teamService.getTeamsByLectureId(lectureId, authInfo);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{teamId}")
  public ResponseEntity<TeamResponseDto> showTeam(
      @PathVariable Long lectureId,
      @PathVariable Long teamId,
      @Auth AuthInfo authInfo
  ) {
    TeamResponseDto response = teamService.getTeamByTeamId(lectureId, teamId, authInfo);

    return ResponseEntity.ok(response);
  }

}
