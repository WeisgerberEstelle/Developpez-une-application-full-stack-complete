package com.openclassrooms.mddapi.exception;

/**
 * Thrown when authentication fails (e.g. invalid credentials).
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
