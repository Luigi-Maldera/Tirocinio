package com.example.demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Recupera il token dalle credenziali dell'autenticazione
        Object credentials = authentication.getCredentials();

        // Verifica del token:
        if (credentials != null && isValidToken(credentials.toString())) {
            // Crea un oggetto di autenticazione con le autorizzazioni appropriate
            return new UsernamePasswordAuthenticationToken(null, credentials, null);
        } else {
            // Token non valido, lancia un'eccezione AuthenticationException
        	throw new CustomAuthenticationException("Invalid token: " + credentials);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // Questo AuthenticationProvider supporta l'autenticazione basata su UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean isValidToken(String token) {
        // Verifica solo se il token non è vuoto
        return token != null && !token.isEmpty();
    }
}
