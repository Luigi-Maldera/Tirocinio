package com.example.demo.testJWTSecurity;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernamePasswordAuthenticationToken extends AbstractAuthenticationToken {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Object principal;
    private Object credentials;

    public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public UsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                              Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // Deve chiamare il costruttore super per impostare l'autenticazione
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    // Altro
}

