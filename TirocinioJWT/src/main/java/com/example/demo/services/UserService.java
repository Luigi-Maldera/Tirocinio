package com.example.demo.services;

import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;

@Service
public class UserService implements UserDetailsService {

    private final PersonaRepository personaRepository;

    @Autowired
    public UserService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Persona persona = personaRepository.findByUsername(username);

        if (persona == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.builder()
                .username(persona.getUsername())
                .password(persona.getPassword())
                .roles("USER")  // Aggiungi eventuali ruoli
                .build();
    }
}