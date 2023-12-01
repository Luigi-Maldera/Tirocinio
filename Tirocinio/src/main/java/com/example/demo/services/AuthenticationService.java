package com.example.demo.services;

import com.example.demo.dto.LoginRequest;
import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;
import com.example.demo.testJWTSecurity.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final PersonaRepository personaRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticationService(PersonaRepository personaRepository, JwtTokenUtil jwtTokenUtil) {
        this.personaRepository = personaRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String authenticate(LoginRequest request) {
        Persona persona = personaRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());

        if (persona != null) {
            // Autenticazione riuscita, genera il token
            return jwtTokenUtil.generateToken(new org.springframework.security.core.userdetails.User(
                    persona.getUsername(),
                    persona.getPassword(),
                    null  // Puoi popolare le autorizzazioni se necessario
            ));
        } else {
            // Restituisci null o una stringa vuota in caso di autenticazione fallita
            return null;
        }
    }
}
