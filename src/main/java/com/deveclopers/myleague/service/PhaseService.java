package com.deveclopers.myleague.service;

import com.deveclopers.myleague.dto.PhaseDto;
import com.deveclopers.myleague.dto.RoundDto;
import com.deveclopers.myleague.mapper.PhaseMapper;
import com.deveclopers.myleague.repository.PhaseRepository;
import com.deveclopers.myleague.repository.RoundRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PhaseService {

  private final RoundService roundService;

  private final PhaseRepository phaseRepository;
  private final RoundRepository roundRepository;

  public PhaseService(
      RoundService roundService, PhaseRepository phaseRepository, RoundRepository roundRepository) {
    this.roundService = roundService;
    this.phaseRepository = phaseRepository;
    this.roundRepository = roundRepository;
  }

  public Flux<PhaseDto> getPhasesByLeagueId(String leagueId) {
    return phaseRepository
        .findByLeagueId(new ObjectId(leagueId))
        .map(PhaseMapper.INSTANCE::instanceToDto);
  }

  public Flux<RoundDto> getRoundsByPhaseId(String phaseId) {
    return roundRepository
        .findByPhaseId(new ObjectId(phaseId))
        .flatMap(
            round ->
                roundService
                    .getMatchesByRoundId(round.getRoundId())
                    .collectList()
                    .map(
                        matches ->
                            new RoundDto(
                                round.getRoundId(),
                                null,
                                "",
                                phaseId,
                                "",
                                round.getOrder(),
                                matches)));
  }
}
