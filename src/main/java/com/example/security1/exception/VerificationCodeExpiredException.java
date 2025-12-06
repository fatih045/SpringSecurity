package com.example.security1.exception;

public class VerificationCodeExpiredException  extends RuntimeException {
    public VerificationCodeExpiredException() {
        super("Verification code has expired    Please request new one ");
    }
}
