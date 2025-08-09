package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Team;
import com.deveclopers.myleague.dto.MatchDto;
import com.deveclopers.myleague.mapper.MatchMapper;
import com.deveclopers.myleague.repository.MatchRepository;
import com.deveclopers.myleague.repository.TeamRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MatchService {

  public final MatchRepository matchRepository;
  public final TeamRepository teamRepository;

  public MatchService(MatchRepository matchRepository, TeamRepository teamRepository) {
    this.matchRepository = matchRepository;
    this.teamRepository = teamRepository;
  }

  public Flux<MatchDto> getMatches() {
    return matchRepository
        .findAll()
        .flatMap(
            match ->
                Mono.zip(
                        teamRepository.findById(match.getHomeTeam().toHexString()),
                        teamRepository.findById(match.getVisitTeam().toHexString()))
                    .map(
                        tuple -> {
                          Team homeTeam = tuple.getT1();
                          Team visitTeam = tuple.getT2();

                          MatchDto matchDtoPrevious = MatchMapper.INSTANCE.instanceToDto(match);

                          return new MatchDto(
                              matchDtoPrevious.id(),
                              homeTeam.getName(),
                              visitTeam.getName(),
                              matchDtoPrevious.homeResult(),
                              matchDtoPrevious.visitResult(),
                              matchDtoPrevious.status());
                        }));
  }
}
