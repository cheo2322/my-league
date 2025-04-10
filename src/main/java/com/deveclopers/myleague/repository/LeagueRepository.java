package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.League;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends ReactiveMongoRepository<League, String> {}
