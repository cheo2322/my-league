package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.service.MyLeagueService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class MyLeagueController {

  private final MyLeagueService myLeagueService;

  public MyLeagueController(MyLeagueService myLeagueService) {
    this.myLeagueService = myLeagueService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<DefaultDto> postLeague(@RequestBody LeagueDto leagueDto) {
    return myLeagueService.createLeague(leagueDto);
  }

  @PostMapping("/{id}/team")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<TeamDto> addTeam(@PathVariable("id") String leagueId, @RequestBody TeamDto teamDto) {
    return myLeagueService.addTeamToLeague(teamDto, leagueId);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<DefaultDto> getLeague() {
    return myLeagueService.getLeagues();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<DefaultDto> getLeague(@PathVariable String id) {
    return myLeagueService.getLeague(id);
  }

  @GetMapping("/{id}/team")
  public ResponseEntity<List<Team>> getTeams(@PathVariable String id) {
    return ResponseEntity.ok(myLeagueService.getTeams(id));
  }
}
