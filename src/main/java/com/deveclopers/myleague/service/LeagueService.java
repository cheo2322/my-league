package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.document.Position;
import com.deveclopers.myleague.document.Positions;
import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.PositionDto;
import com.deveclopers.myleague.dto.PositionsDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.LeagueMapper;
import com.deveclopers.myleague.mapper.TeamMapper;
import com.deveclopers.myleague.repository.LeagueRepository;
import com.deveclopers.myleague.repository.PositionsRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LeagueService {

  private final LeagueRepository leagueRepository;
  private final TeamRepository teamRepository;
  private final PositionsRepository positionsRepository;

  private final PhaseService phaseService;
  private final RoundService roundService;

  private final LeagueMapper LEAGUE_MAPPER = LeagueMapper.INSTANCE;
  private final TeamMapper TEAM_MAPPER = TeamMapper.INSTANCE;

  public LeagueService(
      LeagueRepository leagueRepository,
      TeamRepository teamRepository,
      PositionsRepository positionsRepository,
      PhaseService phaseService,
      RoundService roundService) {
    this.leagueRepository = leagueRepository;
    this.teamRepository = teamRepository;
    this.positionsRepository = positionsRepository;
    this.phaseService = phaseService;
    this.roundService = roundService;
  }

  public Mono<DefaultDto> createLeague(LeagueDto leagueDto) {
    return leagueRepository
        .save(LEAGUE_MAPPER.dtoToLeague(leagueDto))
        .map(LEAGUE_MAPPER::instanceToDefaultDto);
  }

  public Mono<TeamDto> addTeamToLeague(TeamDto teamDto, String leagueId) {
    teamDto.setLeagueId(leagueId);

    return leagueRepository
        .findById(leagueId)
        .flatMap(
            leagueDB ->
                teamRepository
                    .save(TEAM_MAPPER.dtoToTeam(teamDto))
                    .map(
                        teamDB -> {
                          ObjectId teamId = new ObjectId(teamDB.getId());
                          if (leagueDB.getTeams() == null || leagueDB.getTeams().isEmpty()) {
                            leagueDB.setTeams(List.of(teamId));
                          } else {
                            leagueDB.getTeams().add(teamId);
                          }

                          leagueRepository.save(leagueDB).subscribe();

                          return TEAM_MAPPER.instanceToDto(teamDB);
                        }))
        .switchIfEmpty(Mono.error(new RuntimeException()));
  }

  public Flux<TeamDto> getTeamsById(String leagueId) {
    return teamRepository.findByLeagueId(new ObjectId(leagueId)).map(TEAM_MAPPER::instanceToDto);
  }

  public Flux<DefaultDto> getLeagues() {
    return leagueRepository.findAll().map(LEAGUE_MAPPER::instanceToDefaultDto);
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
    return positionsRepository
        .findByLeagueIdAndPhaseIdAndRoundId(
            new ObjectId(leagueId), new ObjectId(phaseId), new ObjectId(roundId))
        .flatMap(
            positions ->
                roundService
                    .getRound(positions.getRoundId().toHexString())
                    .flatMap(
                        round ->
                            Flux.fromIterable(positions.getPositions())
                                .concatMap(
                                    position ->
                                        teamRepository
                                            .findById(position.getTeamId().toHexString())
                                            .map(
                                                team ->
                                                    new PositionDto(
                                                        team.getName(),
                                                        position.getPoints(),
                                                        position.getFavorGoals(),
                                                        position.getAgainstGoals(),
                                                        position.getGoals())))
                                .collectList()
                                .map(
                                    positionDtos ->
                                        new PositionsDto(
                                            positions.getPositionsId(),
                                            round.getOrder(),
                                            positionDtos))))
        .switchIfEmpty(Mono.error(new RuntimeException()));
  }

  // TODO: update when exists
  // TODO: create new when no matches
  public Mono<Void> generatePositions(String leagueId, String phaseId, String roundId) {
    return roundService
        .getRound(roundId)
        .flatMapMany(
            round ->
                phaseService.getRoundsByPhaseId(phaseId).filter(r -> r.order() <= round.getOrder()))
        .flatMap(roundDto -> roundService.getMatchesByRound(roundDto.roundId()))
        .collectList()
        .flatMap(
            matches -> {
              List<Position> positions = new ArrayList<>();
              matches.forEach(match -> assignPositions(match, positions));

              positions.sort(
                  Comparator.comparing(Position::getPoints)
                      .reversed()
                      .thenComparing(Position::getGoals, Comparator.reverseOrder()));

              Positions positionsTable = new Positions();
              positionsTable.setLeagueId(new ObjectId(leagueId));
              positionsTable.setPhaseId(new ObjectId(phaseId));
              positionsTable.setRoundId(new ObjectId(roundId));
              positionsTable.setPositions(positions);

              return positionsRepository.save(positionsTable).then();
            });
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

    positions.sort(
        Comparator.comparing(Position::getPoints)
            .reversed()
            .thenComparing(Position::getGoals, Comparator.reverseOrder()));
  }

  private static Position findPosition(List<Position> positions, ObjectId teamId) {
    return positions.stream().filter(p -> p.getTeamId().equals(teamId)).findFirst().orElse(null);
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
}
