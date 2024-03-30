package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.LeagueMapper;
import com.deveclopers.myleague.mapper.TeamMapper;
import com.deveclopers.myleague.repository.LeagueRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MyLeagueService {

  private final LeagueRepository leagueRepository;
  private final TeamRepository teamRepository;

  public MyLeagueService(LeagueRepository leagueRepository, TeamRepository teamRepository) {
    this.leagueRepository = leagueRepository;
    this.teamRepository = teamRepository;
  }

  public League createLeague(LeagueDto leagueDto) {
    League league = LeagueMapper.INSTANCE.dtoToLeague(leagueDto);

    leagueRepository.save(league);
    return league;
  }

  public List<League> getLeagues() {
    return leagueRepository.findAll();
  }

  public League getLeague(String id) {
    return leagueRepository.findById(id)
      .orElseThrow();
  }

  public List<Team> getTeams(String leagueId) {
    League league = leagueRepository.findById(leagueId).orElseThrow();

    return league.getTeams();
  }

  public Team addTeamToLeague(TeamDto teamDto, String leagueId) {
    League league = leagueRepository.findById(leagueId).orElseThrow();
    Team newTeam = TeamMapper.INSTANCE.dtoToTeam(teamDto);
    Team saved = teamRepository.save(newTeam);

    if (league.getTeams() != null && !league.getTeams().isEmpty()) {
      league.getTeams().add(saved);
    } else {
      league.setTeams(Collections.singletonList(saved));
    }

    leagueRepository.save(league);

    return saved;
  }
}
