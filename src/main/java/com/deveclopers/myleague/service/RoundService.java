package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Round;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.repository.MatchRepository;
import com.deveclopers.myleague.repository.RoundRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    return roundRepository
        .findById(roundId)
        .flatMapMany(
            round ->
                matchRepository
                    .findByRoundId(new ObjectId(round.getRoundId()))
                    .flatMap(
                        match ->
                            Mono.zip(
                                    teamRepository.findById(match.getHomeTeam().toHexString()),
                                    teamRepository.findById(match.getVisitTeam().toHexString()))
                                .map(
                                    tuple -> {
                                      Team homeTeam = tuple.getT1();
                                      Team visitTeam = tuple.getT2();

                                      // TODO: Fix the zoned time
                                      ZonedDateTime matchTime =
                                          match
                                              .getMatchTime()
                                              .atZone(ZoneId.of("America/Guayaquil"));

                                      return new MatchDto(
                                          match.getMatchId(),
                                          homeTeam.getName(),
                                          visitTeam.getName(),
                                          match.getHomeResult(),
                                          match.getVisitResult(),
                                          match.getStatus().name(),
                                          matchTime.format(dateFormatter),
                                          matchTime.format(timeFormatter));
                                    })))
        .switchIfEmpty(Mono.error(new RuntimeException()));
  }

  public Flux<Round> getAllRounds() {
    return roundRepository.findAll();
  }
}
