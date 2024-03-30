package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.dto.LeagueDto;
import com.deveclopers.myleague.mapper.LeagueMapper;
import com.deveclopers.myleague.repository.LeagueRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MyLeagueService {

  private final LeagueRepository leagueRepository;

  public MyLeagueService(LeagueRepository leagueRepository) {
    this.leagueRepository = leagueRepository;
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
}
