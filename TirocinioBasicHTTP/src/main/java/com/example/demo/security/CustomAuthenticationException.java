package com.example.demo.security;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationException extends AuthenticationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
