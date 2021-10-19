package com.exist.exercise08.services.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean 
    public PasswordEncoder passwordEncoder() { 
       return new BCryptPasswordEncoder(); 
    } 


    @Override 
    protected void configure(HttpSecurity http) throws Exception { 
       http 
            // .authorizeRequests()
            //     .antMatchers("/register")
            //         .permitAll()
            //     .anyRequest().authenticated()
            // .and()
            //     .formLogin()
            //     .loginPage("/login")
            //     .permitAll()
            // .and()
            //     .logout()
            // ; 
            .cors().and().csrf().disable() //TODO - CSRF POSSIBLE VULNERABILITY
            .authorizeRequests()
                .antMatchers("/create-employee")
                    .permitAll();
    }
}
