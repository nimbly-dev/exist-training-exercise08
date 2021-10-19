package com.exist.exercise08.services.security;

import java.util.Optional;

import com.exist.exercise08.model.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    Optional<User> findUserByUsername(String username); 
}
