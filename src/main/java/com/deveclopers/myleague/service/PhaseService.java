package com.deveclopers.myleague.service;

import com.deveclopers.myleague.dto.DefaultDto;
import com.deveclopers.myleague.mapper.PhaseMapper;
import com.deveclopers.myleague.repository.PhaseRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PhaseService {

  private final PhaseRepository phaseRepository;

  public PhaseService(PhaseRepository phaseRepository) {
    this.phaseRepository = phaseRepository;
  }

  public Flux<DefaultDto> getPhasesByLeagueId(String leagueId) {
    return phaseRepository
        .findByLeagueId(new ObjectId(leagueId))
        .map(PhaseMapper.INSTANCE::instanceToDefaultDto);
  }
}
