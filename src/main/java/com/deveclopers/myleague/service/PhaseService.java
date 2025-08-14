package com.deveclopers.myleague.service;

import com.deveclopers.myleague.document.Phase;
import com.deveclopers.myleague.dto.PhaseDto;
import com.deveclopers.myleague.mapper.PhaseMapper;
import com.deveclopers.myleague.repository.PhaseRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PhaseService {

  private final PhaseRepository phaseRepository;

  public PhaseService(PhaseRepository phaseRepository) {
    this.phaseRepository = phaseRepository;
  }

  public Flux<PhaseDto> getPhasesByLeagueId(String leagueId) {
    return phaseRepository
        .findByLeagueId(new ObjectId(leagueId))
        .map(PhaseMapper.INSTANCE::instanceToDto);
  }

  public Mono<Phase> getPhase(String phaseId) {
    return phaseRepository.findById(phaseId);
  }
}
