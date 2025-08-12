package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.Positions;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PositionsRepository extends ReactiveMongoRepository<Positions, String> {

  Mono<Positions> findByLeagueIdAndPhaseIdAndRoundId(
      ObjectId leagueId, ObjectId phaseId, ObjectId roundId);
}
