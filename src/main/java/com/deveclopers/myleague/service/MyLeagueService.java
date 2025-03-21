package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.document.Match;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.LeagueMapper;
import com.deveclopers.myleague.mapper.TeamMapper;
import com.deveclopers.myleague.repository.LeagueRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
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
    return leagueRepository.findById(id).orElseThrow();
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

  public void generateRandomMatchDays(String leagueId) {
    League league = leagueRepository.findById(leagueId).orElseThrow();
    List<Team> teams = league.getTeams();
    Collections.shuffle(teams);

    List<Round> rounds = IntStream.range(0, teams.size() - 1)
      .mapToObj(Round::new)
      .toList();

    for (int i = 0; i < teams.size() - 1; i++) {
      for (int j = 0; j < teams.size() - 1; j++) {
        Match match = new Match();
        match.setHome(teams.get(i));
        match.setVisitant(teams.get(j));

        rounds.get(i).getMatches().add(match);
      }
    }
  }
}
