package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.dto.TeamDto;
import com.deveclopers.myleague.mapper.LeagueMapper;
import com.deveclopers.myleague.repository.LeagueRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MyLeagueService {

  private final LeagueRepository leagueRepository;
  private final TeamRepository teamRepository;

  private final LeagueMapper LEAGUE_MAPPER = LeagueMapper.INSTANCE;

  public MyLeagueService(LeagueRepository leagueRepository, TeamRepository teamRepository) {
    this.leagueRepository = leagueRepository;
    this.teamRepository = teamRepository;
  }

  public Mono<DefaultDto> createLeague(LeagueDto leagueDto) {
    return leagueRepository
        .save(LEAGUE_MAPPER.dtoToLeague(leagueDto))
        .map(LEAGUE_MAPPER::instanceToDefaultDto);
  }

  public List<League> getLeagues() {
    //    return leagueRepository.findAll();
    return null;
  }

  public League getLeague(String id) {
    //    return leagueRepository.findById(id).orElseThrow();
    return null;
  }

  public List<Team> getTeams(String leagueId) {
    //    League league = leagueRepository.findById(leagueId).orElseThrow();

    //    return league.getTeams();
    return null;
  }

  public Team addTeamToLeague(TeamDto teamDto, String leagueId) {
    //    League league = leagueRepository.findById(leagueId).orElseThrow();
    //    Team newTeam = TeamMapper.INSTANCE.dtoToTeam(teamDto);
    //    Team saved = teamRepository.save(newTeam);
    //
    //    if (league.getTeams() != null && !league.getTeams().isEmpty()) {
    //      league.getTeams().add(saved);
    //    } else {
    //      league.setTeams(Collections.singletonList(saved));
    //    }
    //
    //    leagueRepository.save(league);
    //
    //    return saved;
    return null;
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
