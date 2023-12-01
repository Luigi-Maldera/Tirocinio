package com.example.demo.controllers;

import com.example.demo.dto.LoginRequest;
import com.example.demo.services.AuthenticationService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationService authenticationService;

    @Autowired
    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
       // String token = authenticationService.authenticate(request);

        //if (token != null) {
            return ResponseEntity.ok(Collections.singletonMap("token", "ciccco"));
      //  } else {
       //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       // }
    }
}
