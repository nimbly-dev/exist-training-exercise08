package com.exist.api.controller;

import com.exist.dao.data.RoleRepository;
import com.exist.dao.data.UserRepository;
import com.exist.model.payload.login.LoginRequestDto;
import com.exist.model.payload.login.SignUpRequestDto;
import com.exist.model.payload.registration.JwtResponseDto;
import com.exist.model.payload.registration.MessageResponseDto;
import com.exist.services.AuthService;
import com.exist.services.impl.UserDetailsImpl;
import com.exist.services.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthService authService;

    @Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin") //TODO - RE-STUDY THIS METHOD
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					loginRequest.getUsername(), 
					loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		

		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponseDto(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/signup") 
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequestDto signUpRequest) {

		authService.checkIfUsernameExist(signUpRequest);
		authService.checkIfEmailExist(signUpRequest);

		authService.checkIfInputsAreBlank(signUpRequest);

		// Create new user's account
		authService.createNewUser(signUpRequest);
		

		return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
	}

    
}
