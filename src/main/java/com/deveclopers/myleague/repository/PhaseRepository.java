package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.Phase;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PhaseRepository extends ReactiveMongoRepository<Phase, String> {

  Flux<Phase> findByLeagueId(ObjectId leagueId);
}
