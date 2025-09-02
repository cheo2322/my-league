package com.deveclopers.myleague.repository.v2;

import com.deveclopers.myleague.document.v2.PositionsV2;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionsRepositoryV2 extends ReactiveMongoRepository<PositionsV2, String> {}
