package com.deveclopers.myleague.service.v2;

import com.deveclopers.myleague.dto.v2.PositionsDtoV2;
import reactor.core.publisher.Mono;

public interface PositionsService {

  /**
   * Generate positions for a specific round.
   *
   * @param roundId the round to generate the positions.
   * @return Void, positions are saved into DB.
   */
  Mono<Void> generatePositions(String roundId);

  /**
   * Get positions from a specific round.
   *
   * @param roundId the round to get positions.
   * @return the positions for the specified round.
   */
  Mono<PositionsDtoV2> getPositions(String roundId);
}
