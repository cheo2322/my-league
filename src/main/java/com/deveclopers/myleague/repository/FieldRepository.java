package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.Field;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends ReactiveMongoRepository<Field, String> {}
