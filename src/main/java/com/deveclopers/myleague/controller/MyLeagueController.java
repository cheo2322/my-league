package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.document.League;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/my_league/v1")
@Slf4j
@CrossOrigin(origins = {"*"})
public class MyLeagueController {

  private final MyLeagueService myLeagueService;

  public MyLeagueController(MyLeagueService myLeagueService) {
    this.myLeagueService = myLeagueService;
  }

  @PostMapping("/league")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<DefaultDto> postLeague(@RequestBody LeagueDto leagueDto) {
    return myLeagueService.createLeague(leagueDto);
  }

  @GetMapping("/league")
  public ResponseEntity<List<League>> getLeague() {
    return ResponseEntity.ok(myLeagueService.getLeagues());
  }

  @GetMapping("/league/{id}")
  public ResponseEntity<League> getLeague(@PathVariable String id) {
    return ResponseEntity.ok(myLeagueService.getLeague(id));
  }

  @GetMapping("league/{id}/team")
  public ResponseEntity<List<Team>> getTeams(@PathVariable String id) {
    return ResponseEntity.ok(myLeagueService.getTeams(id));
  }

  @PatchMapping("/league/{id}/team")
  public ResponseEntity<Team> addTeam(@PathVariable String id, @RequestBody TeamDto teamDto) {
    return ResponseEntity.ok(myLeagueService.addTeamToLeague(teamDto, id));
  }
}
