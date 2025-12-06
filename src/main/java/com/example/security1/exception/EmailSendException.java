package com.example.security1.exception;

public class EmailSendException  extends RuntimeException{
    public EmailSendException(String message) {
        super(message);
    }
}
