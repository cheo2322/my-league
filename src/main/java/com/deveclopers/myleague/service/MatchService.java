package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.dto.MatchDetailsDto;
import com.deveclopers.myleague.repository.MatchRepository;
import com.deveclopers.myleague.security.AuthenticatedUserContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class MatchService {

  private final MatchRepository matchRepository;

  private final RoundService roundService;
  private final PhaseService phaseService;
  private final LeagueService leagueService;

  private final AuthenticatedUserContext userContext;

  public MatchService(
      MatchRepository matchRepository,
      RoundService roundService,
      PhaseService phaseService,
      LeagueService leagueService,
      AuthenticatedUserContext userContext) {
    this.matchRepository = matchRepository;
    this.phaseService = phaseService;
    this.leagueService = leagueService;
    this.roundService = roundService;
    this.userContext = userContext;
  }

  public Mono<Void> updateMatchResult(String matchId, int homeResult, int visitResult) {
    return resolveMatchAndLeague(matchId)
        .flatMap(
            matchAndLeague ->
                mapUserAndUpdate(
                    matchAndLeague.getT1(),
                    matchAndLeague.getT2().getUserOwner().toHexString(),
                    homeResult,
                    visitResult));
  }

  public Mono<MatchDetailsDto> getMatchDetails(String id) {
    return resolveMatchAndLeague(id)
        .flatMap(
            matchAndLeague ->
                validateOwnership(matchAndLeague.getT2().getUserOwner().toHexString())
                    .map(
                        isOwner ->
                            new MatchDetailsDto(matchAndLeague.getT1().getMatchId(), isOwner)));
  }

  /**
   * Check the existence of league to extract the owner.
   *
   * @param matchId related to the league.
   * @return a Tuple with Match and League in case they exist.
   */
  private Mono<Tuple2<Match, League>> resolveMatchAndLeague(String matchId) {
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
                    .map(league -> Tuples.of(match, league)));
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
}
