package com.example.demo.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.Persona;
import com.example.demo.repositories.PersonaRepository;
import com.example.demo.security.CustomAuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;

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
            Authentication authentication = extractCredentials(request);
            if (authentication != null) {
                Authentication result = authenticationManager.authenticate(authentication);
                SecurityContextHolder.getContext().setAuthentication(result);
            }
        } catch (CustomAuthenticationException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            e.printStackTrace();
            System.out.println("Dettagli sull'eccezione: " + e.getMessage());
            return;
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido o scaduto");
            e.printStackTrace();
            System.out.println("Dettagli sull'eccezione: " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private Authentication extractCredentials(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            String token;
            if (header.startsWith("Bearer ")) {
                token = header.substring("Bearer ".length()).trim();
            } else {
                token = header.trim();
            }
            logDebugInfo(request, token);
            if (validateToken(token)) {
                // Use the secret key generated securely
                Claims claims = Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
                String username = claims.getSubject();
                return new UsernamePasswordAuthenticationToken(
                        new User(username, "", Collections.emptyList()), null, Collections.emptyList());
            }
        }
        return null;
    }

   

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            e.printStackTrace();
            System.out.println("Dettagli sull'eccezione: " + e.getMessage());
            throw new CustomAuthenticationException("Firma del token non valida", e);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Dettagli sull'eccezione: " + e.getMessage());
            throw new CustomAuthenticationException("Errore nella validazione del token", e);
        }
    }



    
    private void logDebugInfo(HttpServletRequest request, String token) {
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Token: " + token);
    }

    private Key getSecretKey() {
        // Utilizza la chiave segreta definita nelle proprietà dell'applicazione
        String secretKeyString = "8a8e64f242f6b65e75fb7f2a764db2cf";
        byte[] secretKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

}