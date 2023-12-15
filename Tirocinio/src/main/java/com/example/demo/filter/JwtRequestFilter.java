// JwtRequestFilter.java
package com.example.demo.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.testJWTSecurity.JwtTokenUtil;
import com.example.demo.testJWTSecurity.UserDetailsServiceImpl;
import com.example.demo.testJWTSecurity.UsernamePasswordAuthenticationToken;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try 
            {
            	username = jwtTokenUtil.extractUsername(jwt);
            }catch(Exception e){
            	e.printStackTrace();
            	chain.doFilter(request, response);
            	return;
            }
         //   username = jwtTokenUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                // Aggiungi log per indicare che l'autenticazione è avvenuta con successo
                logger.info("Utente '" + username + "' autenticato con successo.");
            } else {
                // Aggiungi log per indicare che la validazione del token è fallita
                logger.warn("Validazione Token fallita per l'utente '" + username + "'.");
            }
        } else {
            // Aggiungi log per indicare che l'utente non è stato estratto correttamente dal token
            logger.warn("Username not extracted from token or authentication already present in SecurityContextHolder.");
        }

        chain.doFilter(request, response);
    }
}
