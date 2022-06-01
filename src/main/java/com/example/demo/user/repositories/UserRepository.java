package com.example.demo.user.repositories;

// import java.util.Optional;

import com.example.demo.user.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

    // User findOne(String username);
    Boolean existsByUsername(String username);
    User findByUsername(String username);

}
