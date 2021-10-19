package com.exist.exercise08.services.security;

import java.util.Optional;

import com.exist.exercise08.model.user.LoginAttempts;
import com.exist.exercise08.model.user.User;
import com.exist.exercise08.services.SecurityUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {

    private static final int ATTEMPTS_LIMIT = 3; 
    
    @Autowired 
    private SecurityUserDetailsService userDetailsService; 
    @Autowired 
    private PasswordEncoder passwordEncoder; 
    @Autowired 
    private LoginAttemptsRepository attemptsRepository; 
    @Autowired 
    private UserRepository userRepository; 

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        Optional<LoginAttempts> userAttempts = attemptsRepository.findAttemptsByUsername(username); 
        if (userAttempts.isPresent()) { 
           LoginAttempts attempts = userAttempts.get();
           attempts.setAttempts(0); attemptsRepository.save(attempts); 
        }
        return authentication; 
    }


    private void processFailedAttempts(String username, User user) { 
        Optional<LoginAttempts> 
        userAttempts = attemptsRepository.findAttemptsByUsername(username); 
        if (userAttempts.isEmpty()) { 
           LoginAttempts attempts = new LoginAttempts(); 
           attempts.setUsername(username); 
           attempts.setAttempts(1); 
           attemptsRepository.save(attempts); 
        } else {
           LoginAttempts attempts = userAttempts.get(); 
           attempts.setAttempts(attempts.getAttempts() + 1); 
           attemptsRepository.save(attempts);
        
           if (attempts.getAttempts() + 1 > 
           ATTEMPTS_LIMIT) {
              user.setAccountNonLocked(false); 
              userRepository.save(user); 
              throw new LockedException("Too many invalid attempts. Account is locked!!"); 
           }
            
        }
     }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
    
}
