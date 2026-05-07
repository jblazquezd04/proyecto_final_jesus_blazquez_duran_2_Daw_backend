package com.example.backend_torneos.exceptions;

public abstract class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(message);
    }
}
