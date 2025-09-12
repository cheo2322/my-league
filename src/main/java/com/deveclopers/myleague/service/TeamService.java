package com.deveclopers.myleague.service;

import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.TeamMapper;
import com.deveclopers.myleague.repository.TeamRepository;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TeamService {

  private final TeamRepository teamRepository;

  private final TeamMapper TEAM_MAPPER = TeamMapper.INSTANCE;

  public TeamService(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  public Mono<TeamDto> getTeam(String id) {
    return teamRepository
        .findById(id)
        .map(TEAM_MAPPER::instanceToDto)
        .switchIfEmpty(Mono.error(new RuntimeException()));
  }

  public Mono<List<DefaultDto>> getTeamsFromLeague(String leagueId) {
    return teamRepository
        .findByLeagueId(new ObjectId(leagueId))
        .map(TEAM_MAPPER::instanceToDefaultDto)
        .collectList();
  }

  public Flux<TeamDto> getTeamsByLeagueId(String leagueId) {
    return teamRepository.findByLeagueId(new ObjectId(leagueId)).map(TEAM_MAPPER::instanceToDto);
  }
}
