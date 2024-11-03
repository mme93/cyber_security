package org.example.security.exception;

public class MethodNotImplemented extends RuntimeException{

    public MethodNotImplemented(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotImplemented(String message) {
        super(message);
    }
}
