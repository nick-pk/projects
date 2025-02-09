package com.app.auth.exception;

public class JWTAuthenticationException extends RuntimeException {
    public JWTAuthenticationException() {
        super("Invalid JWT token");
    }
}

