package com.deveclopers.myleague.service;

import com.deveclopers.myleague.dto.RoundDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MainService {

  private final RoundService roundService;
  private final PhaseService phaseService;
  private final LeagueService leagueService;

  public MainService(
      RoundService roundService, PhaseService phaseService, LeagueService leagueService) {
    this.roundService = roundService;
    this.phaseService = phaseService;
    this.leagueService = leagueService;
  }

  public Flux<RoundDto> getMainPage() {
    return roundService
        .getAllRounds()
        .flatMap(
            round ->
                Mono.zip(
                        phaseService.getPhase(round.getPhaseId().toHexString()),
                        roundService.getMatchesByRoundId(round.getRoundId()).collectList())
                    .flatMap(
                        tuple -> {
                          var phase = tuple.getT1();
                          var matches = tuple.getT2();

                          return leagueService
                              .getLeagueById(phase.getLeagueId().toHexString())
                              .map(
                                  league ->
                                      new RoundDto(
                                          round.getRoundId(),
                                          league.getLeagueId(),
                                          league.getName(),
                                          phase.getPhaseId(),
                                          phase.getName(),
                                          round.getOrder(),
                                          matches));
                        }));
  }
}
