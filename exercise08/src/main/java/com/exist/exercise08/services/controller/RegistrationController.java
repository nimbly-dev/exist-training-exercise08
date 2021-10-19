package com.exist.exercise08.services.controller;

import java.util.Map;

import com.exist.exercise08.model.user.User;
import com.exist.exercise08.services.SecurityUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    @Autowired 
    private SecurityUserDetailsService userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegistratioForm(){
        return "register"; 
    }

    @PostMapping //TODO add validation
    public void processUserRegistrationForm(@RequestParam Map<String, String> body){
        User user = new User(body.get("username"),passwordEncoder.encode(body.get("password"))); 
        user.setAccountNonLocked(true); 
        userDetailsManager.createUser(user); 
    }

    
}
