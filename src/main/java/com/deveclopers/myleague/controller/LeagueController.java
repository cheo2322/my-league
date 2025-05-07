package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.service.LeagueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/my_league/v1/leagues")
@Slf4j
@CrossOrigin(origins = {"*"})
public class LeagueController {

  private final LeagueService leagueService;

  public LeagueController(LeagueService leagueService) {
    this.leagueService = leagueService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<DefaultDto> postLeague(@RequestBody LeagueDto leagueDto) {
    return leagueService.createLeague(leagueDto);
  }

  @PostMapping("/{id}/team")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<TeamDto> addTeam(@PathVariable("id") String leagueId, @RequestBody TeamDto teamDto) {
    return leagueService.addTeamToLeague(teamDto, leagueId);
  }

  @GetMapping("/{id}/teams")
  @ResponseStatus(HttpStatus.OK)
  public Flux<TeamDto> getTeams(@PathVariable String id) {
    return leagueService.getTeamsFromLeague(id);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<DefaultDto> getLeague() {
    return leagueService.getLeagues();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<LeagueDto> getLeague(@PathVariable String id) {
    return leagueService.getLeague(id);
  }
}
