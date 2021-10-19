package com.exist.exercise08.services.controller;


import javax.validation.Valid;

import com.exist.exercise08.model.registration.RegistrationForm;
import com.exist.exercise08.model.user.User;
import com.exist.exercise08.services.SecurityUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    
    @Autowired 
    private SecurityUserDetailsService userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegistrationForm(RegistrationForm registrationForm){
        return "register"; 
    }

    @PostMapping 
    public String processUserRegistrationForm(
        @Valid RegistrationForm form, BindingResult errors){

        if (errors.hasErrors()) {
            return "register";
        }

        User user = form.toUser(passwordEncoder);
        // User user = new User(body.get("username"),passwordEncoder.encode(body.get("password"))); 
        user.setAccountNonLocked(true); 
        userDetailsManager.createUser(user); 
        return "redirect:/";
    }

    
}
