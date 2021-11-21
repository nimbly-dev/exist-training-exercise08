package com.exist.model.user;

import lombok.Data;

import java.util.List;

@Data
public class LoginResponseDto {
    private Long id;
    private String username;
	private String email;
	private List<String> roles;
    
    public LoginResponseDto(Long id, String username, String email, List<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    
}
