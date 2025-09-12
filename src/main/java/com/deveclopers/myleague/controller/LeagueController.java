package com.deveclopers.myleague.controller;

import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.PhaseDto;
import com.deveclopers.myleague.dto.PositionsDto;
import com.deveclopers.myleague.dto.RoundDto;
import com.deveclopers.myleague.service.LeagueService;
import com.deveclopers.myleague.service.PhaseService;
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
  private final PhaseService phaseService;

  public LeagueController(LeagueService leagueService, PhaseService phaseService) {
    this.leagueService = leagueService;
    this.phaseService = phaseService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<DefaultDto> postLeague(@RequestBody LeagueDto leagueDto) {
    return leagueService.createLeague(leagueDto);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<LeagueDto> getLeagues() {
    return leagueService.getLeagues();
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<LeagueDto> getLeague(@PathVariable String id) {
    return leagueService.getLeague(id);
  }

  @GetMapping("/{id}/phases")
  @ResponseStatus(HttpStatus.OK)
  public Flux<PhaseDto> getDefaultPhases(@PathVariable("id") String leagueId) {
    return phaseService.getPhasesByLeagueId(leagueId);
  }

  @GetMapping("/{id}/positions/{phaseId}/{roundId}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<PositionsDto> getPositions(
      @PathVariable("id") String leagueId,
      @PathVariable("phaseId") String phaseId,
      @PathVariable("roundId") String roundId) {
    return leagueService.getPositions(leagueId, phaseId, roundId);
  }

  // Warning: only for internal use
  @PostMapping("/{id}/positions/{phaseId}/{roundId}")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Void> generatePositions(
      @PathVariable("id") String leagueId,
      @PathVariable("phaseId") String phaseId,
      @PathVariable("roundId") String roundId) {
    return leagueService.generatePositions(leagueId, phaseId, roundId);
  }

  @GetMapping("/{id}/phases/active/rounds")
  @ResponseStatus(HttpStatus.OK)
  public Flux<RoundDto> getRoundsFromActivePhaseByLeagueId(@PathVariable("id") String leagueId) {
    return leagueService.getRoundsFromActivePhaseByLeagueId(leagueId);
  }
}
