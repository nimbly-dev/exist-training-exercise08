package com.exist.services.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public LoginFilter (String urlLogin, String method) {
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(urlLogin, method));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // do your stuff ...

        return super.attemptAuthentication(request, response);
    }
}
