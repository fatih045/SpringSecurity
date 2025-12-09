package com.example.security1.exception;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(String message) {

        super("Refresh token has expired .Please login again .");
    }
}
