package com.deveclopers.myleague.service.v2.impl;

import com.deveclopers.myleague.document.v2.PositionV2;
import com.deveclopers.myleague.dto.v2.PositionsDtoV2;
import com.deveclopers.myleague.repository.v2.PositionsRepositoryV2;
import com.deveclopers.myleague.service.RoundService;
import com.deveclopers.myleague.service.v2.PositionsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PositionsServiceImpl implements PositionsService {

  private final PositionsRepositoryV2 positionsRepository;

  private final RoundService roundService;

  public PositionsServiceImpl(PositionsRepositoryV2 positionsRepository,
    RoundService roundService) {

    this.positionsRepository = positionsRepository;
    this.roundService = roundService;
  }

  @Override
  public Mono<Void> generatePositions(String roundId) {
    List<PositionV2> positions = new ArrayList<>();
    Map<ObjectId, PositionV2> positionsMap = new HashMap<>();

    roundService.getMatchesByRound(roundId)
      .doOnNext(match -> {
        ObjectId homeTeam = match.getHomeTeam();

        if (positionsMap.containsKey(homeTeam)) {

        } else {
          PositionV2 newPosition = new PositionV2(homeTeam);

          // TODO: Resume after matches edition is done
        }

      }).subscribe();

    return Mono.empty();
  }

  @Override
  public Mono<PositionsDtoV2> getPositions(String roundId) {
    return null;
  }
}
