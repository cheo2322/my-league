package com.deveclopers.myleague.repository;

import com.deveclopers.myleague.document.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

  User findByUsername(String username);

  boolean existsByUsername(String username);
}
