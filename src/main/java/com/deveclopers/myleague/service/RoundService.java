package com.deveclopers.myleague.service;

import static com.deveclopers.myleague.constants.MyLeagueConstants.DATE_FORMATTER;
import static com.deveclopers.myleague.constants.MyLeagueConstants.TIME_FORMATTER;

import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.document.Round;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.repository.MatchRepository;
import com.deveclopers.myleague.repository.RoundRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RoundService {

  private final RoundRepository roundRepository;
  private final MatchRepository matchRepository;
  private final TeamRepository teamRepository;

  public RoundService(
      RoundRepository roundRepository,
      MatchRepository matchRepository,
      TeamRepository teamRepository) {
    this.roundRepository = roundRepository;
    this.matchRepository = matchRepository;
    this.teamRepository = teamRepository;
  }

  public Flux<MatchDto> getMatchesByRoundId(String roundId) {
    return roundRepository
        .findById(roundId)
        .flatMapMany(
            round ->
                matchRepository.findByRoundIdOrderByMatchTimeAsc(new ObjectId(round.getRoundId())))
        .concatMap(this::buildMatchDto)
        .switchIfEmpty(Mono.error(new RuntimeException())); // TODO: Replace with custom exception
  }

  public Flux<Round> getAllRounds() {
    return roundRepository.findAllByOrderByOrderAsc();
  }

  public Mono<Round> getRound(String id) {
    return roundRepository.findById(id);
  }

  public Flux<Match> getMatchesByRound(String roundId) {
    return matchRepository.findByRoundIdOrderByMatchTimeAsc(new ObjectId(roundId));
  }

  private Mono<MatchDto> buildMatchDto(Match match) {
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
