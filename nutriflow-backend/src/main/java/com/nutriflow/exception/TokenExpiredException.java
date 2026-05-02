package com.nutriflow.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Token has expired");
    }
}
