package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Positions;
import com.deveclopers.myleague.repository.PositionsRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PositionsService {

  private final PositionsRepository positionsRepository;

  public PositionsService(PositionsRepository positionsRepository) {
    this.positionsRepository = positionsRepository;
  }

  public Mono<Positions> getByLeagueIdAndPhaseIdAndRoundId(
      String leagueId, String phaseId, String roundId) {

    return positionsRepository.findByLeagueIdAndPhaseIdAndRoundId(
        new ObjectId(leagueId), new ObjectId(phaseId), new ObjectId(roundId));
  }

  public Mono<Positions> saveOrUpdate(Positions positions) {
    return positionsRepository.save(positions);
  }
}
