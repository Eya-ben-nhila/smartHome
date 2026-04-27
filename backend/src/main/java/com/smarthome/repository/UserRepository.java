package com.smarthome.repository;

import com.smarthome.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    @Query("{ 'email': ?0, 'isActive': true }")
    Optional<User> findActiveUserByEmail(String email);
    
    @Query("{ 'isActive': true }")
    java.util.List<User> findActiveUsers();
    
    @Query("{ 'id': ?0, 'isActive': true }")
    Optional<User> findActiveUserById(String id);
}
