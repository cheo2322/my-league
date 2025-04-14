package com.deveclopers.myleague.service;

import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.LeagueMapper;
import com.deveclopers.myleague.mapper.TeamMapper;
import com.deveclopers.myleague.repository.LeagueRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MyLeagueService {

  private final LeagueRepository leagueRepository;
  private final TeamRepository teamRepository;

  private final LeagueMapper LEAGUE_MAPPER = LeagueMapper.INSTANCE;
  private final TeamMapper TEAM_MAPPER = TeamMapper.INSTANCE;

  public MyLeagueService(LeagueRepository leagueRepository, TeamRepository teamRepository) {
    this.leagueRepository = leagueRepository;
    this.teamRepository = teamRepository;
  }

  public Mono<DefaultDto> createLeague(LeagueDto leagueDto) {
    return leagueRepository
        .save(LEAGUE_MAPPER.dtoToLeague(leagueDto))
        .map(LEAGUE_MAPPER::instanceToDefaultDto);
  }

  public Mono<TeamDto> addTeamToLeague(TeamDto teamDto, String leagueId) {
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

  public Flux<TeamDto> getTeamsFromLeague(String leagueId) {
    return leagueRepository
        .findById(leagueId)
        .flatMapMany(
            leagueDB ->
                Flux.fromIterable(leagueDB.getTeams())
                    .flatMap(teamId -> teamRepository.findById(teamId.toHexString())))
        .map(TEAM_MAPPER::instanceToDto);
  }

  public Flux<DefaultDto> getLeagues() {
    return leagueRepository.findAll().map(LEAGUE_MAPPER::instanceToDefaultDto);
  }

  public Mono<DefaultDto> getLeague(String id) {
    return leagueRepository
        .findById(id)
        .map(LEAGUE_MAPPER::instanceToDefaultDto)
        .switchIfEmpty(Mono.error(new RuntimeException()));
  }

  public void generateRandomMatchDays(String leagueId) {
    //    League league = leagueRepository.findById(leagueId).orElseThrow();
    //    List<Team> teams = league.getTeams();
    //    Collections.shuffle(teams);
    //
    //    List<Round> rounds = IntStream.range(0, teams.size() - 1)
    //      .mapToObj(Round::new)
    //      .toList();
    //
    //    for (int i = 0; i < teams.size() - 1; i++) {
    //      for (int j = 0; j < teams.size() - 1; j++) {
    //        Match match = new Match();
    //        match.setHome(teams.get(i));
    //        match.setVisitant(teams.get(j));
    //
    //        rounds.get(i).getMatches().add(match);
    //      }
    //    }
  }
}
