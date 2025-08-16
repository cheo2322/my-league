package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.document.Phase;
import com.deveclopers.myleague.document.Position;
import com.deveclopers.myleague.document.Positions;
import com.deveclopers.myleague.document.ProgressStatus;
import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.PositionDto;
import com.deveclopers.myleague.dto.PositionsDto;
import com.deveclopers.myleague.dto.RoundDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.LeagueMapper;
import com.deveclopers.myleague.repository.LeagueRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LeagueService {

  private final LeagueRepository leagueRepository;

  private final PhaseService phaseService;
  private final RoundService roundService;
  private final TeamService teamService;
  private final PositionsService positionsService;

  private final LeagueMapper LEAGUE_MAPPER = LeagueMapper.INSTANCE;

  public LeagueService(
      LeagueRepository leagueRepository,
      PhaseService phaseService,
      RoundService roundService,
      TeamService teamService,
      PositionsService positionsService) {

    this.leagueRepository = leagueRepository;
    this.phaseService = phaseService;
    this.roundService = roundService;
    this.teamService = teamService;
    this.positionsService = positionsService;
  }

  public Mono<DefaultDto> createLeague(LeagueDto leagueDto) {
    return leagueRepository
        .save(LEAGUE_MAPPER.dtoToLeague(leagueDto))
        .map(LEAGUE_MAPPER::instanceToDefaultDto);
  }

  public Flux<LeagueDto> getLeagues() {
    return leagueRepository.findAll().map(LEAGUE_MAPPER::instanceToDto);
  }

  public Mono<LeagueDto> getLeague(String id) {
    return leagueRepository
        .findById(id)
        .map(LEAGUE_MAPPER::instanceToDto)
        .switchIfEmpty(Mono.error(new RuntimeException()));
  }

  public Mono<League> getLeagueById(String id) {
    return leagueRepository.findById(id);
  }

  public Mono<PositionsDto> getPositions(String leagueId, String phaseId, String roundId) {
    return positionsService
        .getByLeagueIdAndPhaseIdAndRoundId(leagueId, phaseId, roundId)
        .flatMap(
            positions ->
                roundService
                    .getRound(positions.getRoundId().toHexString())
                    .flatMap(
                        round ->
                            Flux.fromIterable(positions.getPositions())
                                .concatMap(
                                    position ->
                                        teamService
                                            .getTeam(position.getTeamId().toHexString())
                                            .map(team -> buildPositionDto(position, team)))
                                .collectList()
                                .map(
                                    positionDtos ->
                                        new PositionsDto(
                                            positions.getPositionsId(),
                                            round.getOrder(),
                                            positionDtos))))
        .switchIfEmpty(Mono.empty());
  }

  public Mono<Void> generatePositions(String leagueId, String phaseId, String roundId) {
    return Mono.zip(leagueRepository.findById(leagueId), phaseService.getPhase(phaseId))
        .flatMap(
            objects -> {
              League league = objects.getT1();
              Phase phase = objects.getT2();

              return roundService
                  .getFromLeagueAndPhase(league, phase)
                  .flatMap(roundDto -> roundService.getMatchesByRound(roundDto.roundId()))
                  .collectList()
                  .flatMap(matches -> mapMatches(roundId, matches, league, phase));
            });
  }

  public Flux<RoundDto> getRoundsFromActivePhaseByLeagueId(String leagueId) {
    return leagueRepository
        .findById(leagueId)
        .flatMapMany(
            league ->
                phaseService
                    .getPhase(league.getActivePhaseId().toHexString())
                    .flatMapMany(
                        phase ->
                            roundService
                                .getFromLeagueAndPhase(league, phase)
                                .collectList()
                                .map(
                                    list ->
                                        list.stream()
                                            .sorted(Comparator.comparing(RoundDto::order))
                                            .collect(Collectors.toList()))
                                .flatMapMany(Flux::fromIterable)));
  }

  private static PositionDto buildPositionDto(Position position, TeamDto team) {
    return new PositionDto(
        team.getName(),
        position.getPositionStatus() != null
            ? position.getPositionStatus().name()
            : "", // TODO: Fix it! Shouldn't be null position status
        position.getPlayedGames(),
        position.getPoints(),
        position.getFavorGoals(),
        position.getAgainstGoals(),
        String.format("%s%d", position.getGoals() > 0 ? "+" : "", position.getGoals()));
  }

  private Mono<Void> mapMatches(String roundId, List<Match> matches, League league, Phase phase) {
    List<Position> positions = new ArrayList<>();
    matches.stream()
        .filter(match -> !match.getStatus().equals(ProgressStatus.SCHEDULED))
        .forEach(match -> assignPositions(match, positions));

    return teamService
        .getTeamsByLeagueId(league.getLeagueId())
        .filter(isTeamMissingInPositions(positions))
        .doOnNext(teamDto -> positions.add(new Position(teamDto.getId())))
        .collectList()
        .then(
            Mono.defer(
                () ->
                    updateOrCreatePositions(
                        positions, league.getLeagueId(), phase.getPhaseId(), roundId)));
  }

  private Mono<Void> updateOrCreatePositions(
      List<Position> positions, String leagueId, String phaseId, String roundId) {

    positions.sort(
        Comparator.comparing(Position::getPoints)
            .reversed()
            .thenComparing(Position::getGoals, Comparator.reverseOrder())
            .thenComparing(Position::getFavorGoals, Comparator.reverseOrder())
            .thenComparing(Position::getAgainstGoals)
            .thenComparing(Position::getPlayedGames));

    return positionsService
        .getByLeagueIdAndPhaseIdAndRoundId(leagueId, phaseId, roundId)
        .flatMap(
            positionsDB -> {
              positionsDB.setPositions(positions);
              return positionsService.saveOrUpdate(positionsDB);
            })
        .switchIfEmpty(
            Mono.defer(
                () -> {
                  Positions positionsTable = new Positions();
                  positionsTable.setLeagueId(new ObjectId(leagueId));
                  positionsTable.setPhaseId(new ObjectId(phaseId));
                  positionsTable.setRoundId(new ObjectId(roundId));
                  positionsTable.setPositions(positions);

                  return positionsService.saveOrUpdate(positionsTable);
                }))
        .then();
  }

  private void assignPositions(Match match, List<Position> positions) {
    int homeGoals = match.getHomeResult();
    int visitGoals = match.getVisitResult();

    int pointsToAddToHomeTeam = homeGoals > visitGoals ? 3 : homeGoals < visitGoals ? 0 : 1;
    int pointsToAddToVisitTeam = visitGoals > homeGoals ? 3 : visitGoals < homeGoals ? 0 : 1;

    Position homeTeam = findPosition(positions, match.getHomeTeam());
    assignPoints(
        match.getMatchId(),
        positions,
        homeTeam,
        match.getHomeTeam(),
        pointsToAddToHomeTeam,
        homeGoals,
        visitGoals);

    Position visitTeam = findPosition(positions, match.getVisitTeam());
    assignPoints(
        match.getMatchId(),
        positions,
        visitTeam,
        match.getVisitTeam(),
        pointsToAddToVisitTeam,
        visitGoals,
        homeGoals);
  }

  private static Position findPosition(List<Position> positions, ObjectId teamId) {
    return positions.stream()
        .filter(position -> position.getTeamId().equals(teamId))
        .findFirst()
        .orElse(null);
  }

  private static void assignPoints(
      String matchId,
      List<Position> positions,
      Position teamPosition,
      ObjectId teamId,
      int pointsToAdd,
      int favorGoals,
      int againstGoals) {

    if (teamPosition == null) {
      teamPosition = new Position(matchId);
      teamPosition.setTeamId(teamId);
      positions.add(teamPosition);
    }

    teamPosition.setPlayedGames(teamPosition.getPlayedGames() + 1);
    teamPosition.setPoints(teamPosition.getPoints() + pointsToAdd);
    teamPosition.setFavorGoals(teamPosition.getFavorGoals() + favorGoals);
    teamPosition.setAgainstGoals(teamPosition.getAgainstGoals() + againstGoals);
    teamPosition.setGoals(teamPosition.getFavorGoals() - teamPosition.getAgainstGoals());
  }

  private static Predicate<TeamDto> isTeamMissingInPositions(List<Position> positions) {
    return teamDto ->
        positions.stream()
            .noneMatch(position -> position.getTeamId().toHexString().equals(teamDto.getId()));
  }
}
