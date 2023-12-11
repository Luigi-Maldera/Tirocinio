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

        // Verifica se il token non è null prima di procedere
        if (credentials == null || credentials.toString().isEmpty()) {
            throw new CustomAuthenticationException("Token is null or empty");
        }

        // Puoi effettuare ulteriori controlli personalizzati qui
        // Ad esempio, verifica se il token è valido

        // Esempio di verifica del token (questo è solo un esempio, personalizzalo secondo le tue esigenze):
        if (isValidToken(credentials.toString())) {
            // Crea un oggetto di autenticazione con le autorizzazioni appropriate
            return new UsernamePasswordAuthenticationToken(null, credentials, null);
        } else {
            // Token non valido, lancia un'eccezione AuthenticationException
            throw new CustomAuthenticationException("Invalid token: " + credentials.toString());
        }
    }
    

    @Override
    public boolean supports(Class<?> authentication) {
        // Questo AuthenticationProvider supporta l'autenticazione basata su UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean isValidToken(String token) {
        return token != null && !token.trim().isEmpty();
    }

}
