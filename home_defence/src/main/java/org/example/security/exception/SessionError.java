package org.example.security.exception;

public class SessionError extends RuntimeException{

    public SessionError(String message) {
        super(message);
    }

    public SessionError(String message, Throwable cause) {
        super(message, cause);
    }
}
