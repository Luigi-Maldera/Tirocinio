// JwtTokenUtil.java
package com.example.demo.testJWTSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.demo.model.Persona;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil {

   // @Value("${jwt.secret}")
    private String secret = "hippo23456789876543234567892345678234567898765432345678998765434567890";

    @Value("${jwt.expiration}")
    private Long expiration;
    
    private final JwtParser jwtParser;
    
    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    
    public JwtTokenUtil() {
    	this.jwtParser = Jwts.parser().setSigningKey(secret);
    	//this.secret = generateSecretKey();
    }

    public String createToken(Persona persona) {
    	
    	Claims claims = Jwts.claims();
        claims.put("firstName", persona.getNome());
        claims.put("lastName", persona.getCognome());
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + 600000);
        //TimeUnit.MINUTES.toMillis(expiration));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(persona.getUsername())
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .signWith(getSigningKey())
                .compact();
    }
    
    private Key getSigningKey() {
    	  byte[] keyBytes = Decoders.BASE64.decode(this.secret);
    	  return Keys.hmacShaKeyFor(keyBytes);
    	}
    
    private String generateSecretKey() {
        byte[] secretKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        return Base64.getEncoder().encodeToString(secretKeyBytes);
    }

    private Claims parseJwtClaims(String token) {
        //return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return jwtParser.parseClaimsJws(token).getBody();
    }
    
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }
    
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    public String getEmail(Claims claims) {
        return claims.getSubject();
    }

    public String extractUsername(String token) {
        return parseJwtClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = parseJwtClaims(token).getExpiration();
        return expirationDate.before(new Date());
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}
