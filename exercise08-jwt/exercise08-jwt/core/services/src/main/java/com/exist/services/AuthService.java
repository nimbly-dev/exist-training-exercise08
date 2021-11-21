package com.exist.services;

import com.exist.dao.data.RoleRepository;
import com.exist.dao.data.UserRepository;
import com.exist.model.payload.login.SignUpRequestDto;
import com.exist.model.user.ERole;
import com.exist.model.user.Role;
import com.exist.model.user.User;
import com.exist.services.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional(rollbackOn  = { RuntimeException.class })
@Service
public class AuthService {

    @Autowired 
	AuthenticationManager authenticationManager;

    @Autowired
	JwtUtils jwtUtils;
    
	@Autowired
	RoleRepository roleRepository;

    @Autowired
	PasswordEncoder encoder;

	@Autowired
	UserRepository userRepository;

    public void checkIfUsernameExist(SignUpRequestDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			throw new ResponseStatusException
                (HttpStatus.BAD_REQUEST,"Error: Username is already taken!");
		}
    }

    public void checkIfEmailExist(SignUpRequestDto signUpRequest){
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new ResponseStatusException
                (HttpStatus.BAD_REQUEST,"Error: Email is already taken!");
		}
    }

    public void checkIfInputsAreBlank(SignUpRequestDto signUpRequest){
        if( signUpRequest.getUsername().isEmpty() ||
            signUpRequest.getPassword().isEmpty() ||
            signUpRequest.getEmail().isEmpty()    ||
            signUpRequest.getRole().isEmpty()){
                throw new ResponseStatusException
                    (HttpStatus.BAD_REQUEST,"Error: Inputs must not be null");
        }		
    }

    public void createNewUser(SignUpRequestDto signUpRequest){
		User user = new User(signUpRequest.getUsername(),
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found. Rollbacking now"));
						roles.add(adminRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found. Rollbacking now"));
						roles.add(userRole);
				}
			});
		}
        user.setRoles(roles);
		userRepository.save(user);
    }

}
