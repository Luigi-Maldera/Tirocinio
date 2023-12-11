package com.example.demo.security;

import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class CustomAuthenticationManager extends ProviderManager {

    public CustomAuthenticationManager(CustomAuthenticationProvider customAuthenticationProvider) {
        super(List.of(customAuthenticationProvider));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            return super.authenticate(authentication);
        } catch (ProviderNotFoundException e) {
            // Se nessun provider può gestire l'autenticazione
            throw new CustomAuthenticationException("Authentication failed", e);
        }
    }
}
