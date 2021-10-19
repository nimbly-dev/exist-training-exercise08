package com.exist.exercise08.model.registration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.exist.exercise08.model.user.User;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class RegistrationForm {

    @NotBlank(message = "Username is required")
    @Size(min=2, max=30)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public User toUser(PasswordEncoder passwordEncoder){
        return new User(username, passwordEncoder.encode(password));
    }
}
