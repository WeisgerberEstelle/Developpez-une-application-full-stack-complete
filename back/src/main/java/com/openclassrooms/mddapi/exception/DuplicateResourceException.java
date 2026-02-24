package com.openclassrooms.mddapi.exception;

/**
 * Thrown when attempting to create or update a resource that conflicts with an existing one
 * (e.g. duplicate email, username, or subscription).
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
