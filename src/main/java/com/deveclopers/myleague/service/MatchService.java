package com.deveclopers.myleague.service;

import static com.deveclopers.myleague.constants.MyLeagueConstants.DATE_FORMATTER;
import static com.deveclopers.myleague.constants.MyLeagueConstants.TIME_FORMATTER;

import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.repository.MatchRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MatchService {

  public final MatchRepository matchRepository;
  public final TeamRepository teamRepository;

  public MatchService(MatchRepository matchRepository, TeamRepository teamRepository) {
    this.matchRepository = matchRepository;
    this.teamRepository = teamRepository;
  }

  // TODO: Need to be deprecated
  @Deprecated
  public Flux<MatchDto> getMatches() {
    return matchRepository.findAll().flatMap(this::mapMatch);
  }

  public Mono<MatchDto> getMatch(String matchId) {
    return matchRepository.findById(matchId).flatMap(this::mapMatch);
  }

  private Mono<MatchDto> mapMatch(Match match) {
    return Mono.zip(
            teamRepository.findById(match.getHomeTeam().toHexString()),
            teamRepository.findById(match.getVisitTeam().toHexString()))
        .map(
            tuple -> {
              Team homeTeam = tuple.getT1();
              Team visitTeam = tuple.getT2();

              // TODO: Fix the zoned time
              ZonedDateTime matchTime = match.getMatchTime().atZone(ZoneId.of("America/Guayaquil"));

              return new MatchDto(
                  match.getMatchId(),
                  homeTeam.getName(),
                  visitTeam.getName(),
                  match.getHomeResult(),
                  match.getVisitResult(),
                  match.getStatus().name(),
                  matchTime.format(DATE_FORMATTER),
                  matchTime.format(TIME_FORMATTER));
            });
  }
}
