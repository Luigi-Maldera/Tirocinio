package com.example.demo.services;

import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;
import com.example.demo.security.CustomAuthenticationException;
import com.example.demo.security.SecurityConfig;

@Service
public class AuthenticationService {

    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(
            PersonaRepository personaRepository,
            @Lazy PasswordEncoder passwordEncoder) {
        this.personaRepository = personaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        Persona persona = personaRepository.findByUsername(username);

        if (persona != null && password.equals(persona.getPassword())) {
            return generateToken(persona);
        } else {
            return null;
        }
    }

    private String generateToken(Persona persona) {
        try {
            String secretKeyString = "8a8e64f242f6b65e75fb7f2a764db2cf";
            byte[] secretKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);

            String token = Jwts.builder()
                    .setSubject(persona.getUsername())
                    .signWith(Keys.hmacShaKeyFor(secretKeyBytes), SignatureAlgorithm.HS256)
                    .compact();

            // Salva il token nel campo token della Persona
            persona.setToken(token);
            personaRepository.save(persona);

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Dettagli sull'eccezione: " + e.getMessage());
            throw new CustomAuthenticationException("Errore nella generazione del token", e);
        }
    }


}