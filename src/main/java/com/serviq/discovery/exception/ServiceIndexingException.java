package com.serviq.discovery.exception;

public class ServiceIndexingException extends RuntimeException {
    public ServiceIndexingException(String message) {
        super(message);
    }

    public ServiceIndexingException(String message, Throwable cause) {
        super(message, cause);
    }
}
