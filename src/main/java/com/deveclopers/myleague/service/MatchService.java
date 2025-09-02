package com.deveclopers.myleague.service;

import static com.deveclopers.myleague.constants.MyLeagueConstants.DATE_FORMATTER;
import static com.deveclopers.myleague.constants.MyLeagueConstants.TIME_FORMATTER;

import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.repository.MatchRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import com.deveclopers.myleague.security.AuthenticatedUserContext;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MatchService {

  private final MatchRepository matchRepository;
  private final TeamRepository teamRepository;

  private final RoundService roundService;
  private final PhaseService phaseService;
  private final LeagueService leagueService;

  private final AuthenticatedUserContext userContext;

  public MatchService(
      MatchRepository matchRepository,
      TeamRepository teamRepository,
      RoundService roundService,
      PhaseService phaseService,
      LeagueService leagueService,
      AuthenticatedUserContext userContext) {
    this.matchRepository = matchRepository;
    this.teamRepository = teamRepository;
    this.phaseService = phaseService;
    this.leagueService = leagueService;
    this.roundService = roundService;
    this.userContext = userContext;
  }

  public Mono<MatchDto> getMatch(String matchId) {
    return matchRepository.findById(matchId).flatMap(this::mapMatch);
  }

  public Mono<Void> updateMatchResult(String matchId, int homeResult, int visitResult) {
    return matchRepository
        .findById(matchId)
        .switchIfEmpty(Mono.error(new RuntimeException("Not Found Match")))
        .flatMap(
            match ->
                roundService
                    .getRound(match.getRoundId().toHexString())
                    .flatMap(round -> phaseService.getPhase(round.getPhaseId().toHexString()))
                    .flatMap(
                        phase -> leagueService.getLeagueById(phase.getLeagueId().toHexString()))
                    .flatMap(
                        league ->
                            mapUserAndUpdate(
                                match,
                                league.getUserOwner().toHexString(),
                                homeResult,
                                visitResult)));
  }

  /**
   * Map the user to validate the ownership and update the result.
   *
   * @param match the match to update.
   * @param owner the ownerId to validate the ownership.
   * @param homeResult the updated home result.
   * @param visitResult the updated visit result.
   * @return Void.
   */
  private Mono<Void> mapUserAndUpdate(Match match, String owner, int homeResult, int visitResult) {
    return validateOwnership(owner)
        .filter(Boolean::booleanValue)
        .switchIfEmpty(Mono.error(new RuntimeException("Unauthorized: Not the owner")))
        .flatMap(
            ignored -> {
              match.setHomeResult(homeResult);
              match.setVisitResult(visitResult);
              return matchRepository.save(match).then();
            });
  }

  /**
   * Validates if the League's owner matches with the user context.
   *
   * @param ownerId to validate.
   * @return True if the owner matches, False otherwise.
   */
  private Mono<Boolean> validateOwnership(String ownerId) {
    return userContext.getUserId().map(userId -> userId.equals(ownerId));
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
