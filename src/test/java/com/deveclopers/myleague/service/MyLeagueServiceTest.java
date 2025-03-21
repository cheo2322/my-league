package com.deveclopers.myleague.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.openMocks;

import com.deveclopers.myleague.document.League;
import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.repository.LeagueRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyLeagueServiceTest {

  @Mock LeagueRepository leagueRepository;

  @Mock TeamRepository teamRepository;

  @InjectMocks MyLeagueService service;

  @BeforeEach
  void setUp() {
    openMocks(this);
  }

  @Test
  void createLeague() {}

  @Test
  void getLeagues() {}

  @Test
  void getLeague() {}

  @Test
  void getTeams() {}

  @Test
  void addTeamToLeague() {}

  @Test
  void generateRandomMatchDays() {
    League league = new League();
    league.setLeagueId("0");

    Team team1 = new Team();
    Team team2 = new Team();
    List<Team> teams = List.of(team1, team2);
    league.setTeams(teams);

    Mockito.when(leagueRepository.findById(any()))
      .thenReturn(Optional.of(league));

    service.generateRandomMatchDays("0");
  }
}
