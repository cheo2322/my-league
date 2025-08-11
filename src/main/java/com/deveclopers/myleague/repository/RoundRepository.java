package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.Round;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface RoundRepository extends ReactiveMongoRepository<Round, String> {

  Flux<Round> findByPhaseId(ObjectId phaseId);

  Flux<Round> findAllByOrderByOrderAsc();
}
