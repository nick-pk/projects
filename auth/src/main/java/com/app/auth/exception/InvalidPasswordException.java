package com.app.auth.exception;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException() {
        super("Password is not strong enough");
    }
}
