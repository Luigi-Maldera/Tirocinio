package com.example.demo.controllers;

import com.example.demo.dto.LoginRequest;
import com.example.demo.response.LoginResponse;
import com.example.demo.response.ErrorResponse;
import com.example.demo.model.Persona;
import com.example.demo.services.AuthenticationService;
import com.example.demo.testJWTSecurity.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final AuthenticationService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public LoginController(AuthenticationService authenticationService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationService = authenticationService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication =
                    authenticationService.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String username = authentication.getName();
            Persona user = authenticationService.loadUserByUsername(username);

            String token = jwtTokenUtil.createToken(user);
            LoginResponse loginRes = new LoginResponse(username, token);

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
