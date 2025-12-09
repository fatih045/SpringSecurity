package com.example.security1.exception;

public class InvalidVerificationException extends RuntimeException {
    public InvalidVerificationException() {
        super("Invalid verification code. Please check and try again.");
    }
}
