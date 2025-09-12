package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Phase;
import com.deveclopers.myleague.document.Round;
import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.dto.RoundDto;
import com.deveclopers.myleague.dto.favourite.FavouriteDto;
import com.deveclopers.myleague.dto.favourite.FavouriteLeague;
import com.deveclopers.myleague.security.AuthenticatedUserContext;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MainService {

  private final RoundService roundService;
  private final PhaseService phaseService;
  private final LeagueService leagueService;
  private final UserService userService;
  private final AuthenticatedUserContext userContext;

  public MainService(
      RoundService roundService,
      PhaseService phaseService,
      LeagueService leagueService,
      UserService userService,
      AuthenticatedUserContext userContext) {
    this.roundService = roundService;
    this.phaseService = phaseService;
    this.leagueService = leagueService;
    this.userService = userService;
    this.userContext = userContext;
  }

  public Mono<FavouriteDto> getFavourites() {
    return userContext
        .getUserId()
        .flatMap(
            userId ->
                userService
                    .getUser(userId)
                    .flatMap(
                        user ->
                            Flux.fromIterable(user.getFavouriteLeagues())
                                .flatMap(this::buildFavouriteLeague)
                                .collectList()
                                .map(
                                    favouriteLeagues ->
                                        new FavouriteDto(userId, favouriteLeagues, List.of()))));
  }

  private Mono<FavouriteLeague> buildFavouriteLeague(ObjectId leagueId) {
    return leagueService
        .getLeagueById(leagueId.toHexString())
        .flatMap(
            league ->
                roundService
                    .getRound(league.getActiveRoundId().toHexString())
                    .flatMap(
                        round ->
                            buildRoundDto(round)
                                .map(
                                    roundDto ->
                                        new FavouriteLeague(
                                            league.getLeagueId(),
                                            league.getName(),
                                            league.getHasStarted(),
                                            roundDto))));
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
