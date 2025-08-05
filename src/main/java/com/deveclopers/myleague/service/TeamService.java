package com.deveclopers.myleague.service;

import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.TeamMapper;
import com.deveclopers.myleague.repository.TeamRepository;
import org.springframework.stereotype.Service;
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
}
