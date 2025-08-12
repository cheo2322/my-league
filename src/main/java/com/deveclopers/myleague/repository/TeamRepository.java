package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.Team;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TeamRepository extends ReactiveMongoRepository<Team, String> {

  Flux<Team> findByLeagueId(ObjectId leagueId);
}
