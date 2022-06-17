package com.exist.api.controller;

import com.exist.model.payload.registration.MessageResponseDto;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/")
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(new MessageResponseDto("Succesfully getted"));
    }
}
