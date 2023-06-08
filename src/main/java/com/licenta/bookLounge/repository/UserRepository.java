package com.licenta.bookLounge.repository;

import com.licenta.bookLounge.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	Optional<User> findByUsername(String username);
}