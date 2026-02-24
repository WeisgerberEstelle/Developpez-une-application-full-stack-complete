package com.openclassrooms.mddapi.exception;

/**
 * Thrown when a requested resource (user, post, topic, etc.) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
