package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Phase;
import com.deveclopers.myleague.document.Round;
import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.dto.RoundDto;
import java.util.List;
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
    return leagueService
        .getLeagues()
        .flatMap(
            leagueDto ->
                roundService.getRound(leagueDto.activeRoundId()).flatMapMany(this::buildRoundDto));
  }

  private Mono<RoundDto> buildRoundDto(Round round) {
    return Mono.zip(
            phaseService.getPhase(round.getPhaseId().toHexString()),
            roundService.getMatchesByRoundId(round.getRoundId()).collectList())
        .flatMap(tuple -> assembleRoundDto(round, tuple.getT1(), tuple.getT2()));
  }

  private Mono<RoundDto> assembleRoundDto(Round round, Phase phase, List<MatchDto> matches) {
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
  }
}
