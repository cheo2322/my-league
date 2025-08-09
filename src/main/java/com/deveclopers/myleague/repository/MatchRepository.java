package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.Match;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MatchRepository extends ReactiveMongoRepository<Match, String> {

  Flux<Match> findByRoundId(ObjectId roundId);
}
