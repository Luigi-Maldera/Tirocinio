package com.example.demo.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final PersonaRepository personaRepository;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, PersonaRepository personaRepository) {
        this.authenticationManager = authenticationManager;
        this.personaRepository = personaRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Estrai il token dall'header della richiesta
            Authentication authentication = extractCredentials(request);

            // Verifica e imposta l'autenticazione nel contesto di sicurezza
            if (authentication != null) {
                Authentication result = authenticationManager.authenticate(authentication);
                SecurityContextHolder.getContext().setAuthentication(result);
            }
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            e.printStackTrace();
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Authentication extractCredentials(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Basic ")) {
            String token = header.substring("Basic ".length()).trim();
            if (validateToken(token)) {
                return new UsernamePasswordAuthenticationToken(null, null);
            }
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            // Decodifica il token (stessa logica usata per generare il token)
            String decodedToken = new String(Base64Utils.decodeFromString(token));

            // Estrai le informazioni dal token
            String[] tokenParts = decodedToken.split(":");
            String username = tokenParts[0];
            String password = tokenParts[1];

            // Cerca la Persona nel database usando le informazioni estratte
            Persona persona = personaRepository.findByUsername(username);

            // Verifica che la Persona esista e che la password corrisponda
            return persona != null && password.equals(persona.getPassword());
        } catch (Exception e) {
            // Gestisci eventuali eccezioni (ad esempio, token malformato)
            return false;
        }
    }
}



    /*private Authentication extractCredentials(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Basic ")) {
            String base64Credentials = header.substring("Basic ".length()).trim();
            String credentials = new String(Base64Utils.decodeFromString(base64Credentials));
            return new UsernamePasswordAuthenticationToken(null, credentials);
        }
        return null;
    }

}*/
