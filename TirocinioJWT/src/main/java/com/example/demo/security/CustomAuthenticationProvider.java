package com.example.demo.security;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.demo.filter.TokenAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.security.Keys;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	 @Override
	 public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	     // Recupera il token dalle credenziali dell'autenticazione
	     Object credentials = authentication.getCredentials();

	     // Esempio di verifica del token (questo è solo un esempio, personalizzalo secondo le tue esigenze):
	     if (credentials != null && isValidToken(credentials.toString())) {
	         // Crea un oggetto di autenticazione con le autorizzazioni appropriate
	         return new UsernamePasswordAuthenticationToken(null, credentials, null);
	     } else {
	         // Token non valido, lancia un'eccezione AuthenticationException con un messaggio specifico
	         throw new CustomAuthenticationException("Token non valido o scaduto");
	     }
	 }

    @Override
    public boolean supports(Class<?> authentication) {
        // Questo AuthenticationProvider supporta l'autenticazione basata su UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean isValidToken(String token) {
        try {
            // Usa la chiave segreta definita nelle proprietà dell'applicazione
            String secretKeyString = "8a8e64f242f6b65e75fb7f2a764db2cf";
            byte[] secretKeyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);

            Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secretKeyBytes)).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // Logga ulteriori dettagli sull'eccezione
            e.printStackTrace();
            System.out.println("Dettagli sull'eccezione: " + e.getMessage());
            return false;
        } catch (Exception e) {
            // Logga ulteriori dettagli sull'eccezione
            e.printStackTrace();
            System.out.println("Dettagli sull'eccezione: " + e.getMessage());
            return false;
        }
    }
}
