package com.exist.services.config;

import com.exist.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("user")
				.password("{noop}12345678")
				.roles("USER")
				.and()
				.withUser("admin")
				.password("{noop}87654321")
				.roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/api/ticket/**").hasRole("USER")
				.antMatchers("/api/employee/**").hasRole("USER")
				.antMatchers("/api/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.httpBasic()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

}
