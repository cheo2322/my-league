package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.Match;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MatchRepository extends ReactiveMongoRepository<Match, String> {}
