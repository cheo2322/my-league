package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/my_league/v1/teams")
@Slf4j
@CrossOrigin(origins = {"*"})
public class TeamController {

  private final TeamService teamService;

  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<TeamDto> getTeam(@PathVariable("id") String teamId) {
    return teamService.getTeam(teamId);
  }
}
