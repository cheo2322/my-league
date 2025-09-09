package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.League;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LeagueRepository extends ReactiveMongoRepository<League, String> {

  Flux<League> findByUserOwner(ObjectId userOwner);
}
