package com.exist.exercise08.model.user;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC, force=true)
@RequiredArgsConstructor
public class LoginAttempts {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;

    private String username; 
    private int attempts;

}
