package com.exist.exercise08.services.security;

import java.util.Optional;

import com.exist.exercise08.model.user.LoginAttempts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptsRepository extends JpaRepository<LoginAttempts, Integer> {
    Optional<LoginAttempts> findAttemptsByUsername(String username); 
}
