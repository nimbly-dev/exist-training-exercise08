package com.exist.exercise08.services.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class WebController implements WebMvcConfigurer {

    @Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/register").setViewName("register");
		registry.addViewController("/login").setViewName("login");
	}
}
