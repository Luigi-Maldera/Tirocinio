package com.example.demo.testJWTSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PersonaRepository userRepository;  
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Persona user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Utente non trovato con username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), null);
    }
}
