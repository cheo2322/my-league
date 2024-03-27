package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.League;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends MongoRepository<League, String> {

}
