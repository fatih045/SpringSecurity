package com.example.security1.exception;

public class EmailNotVerifiedException   extends RuntimeException{
    public EmailNotVerifiedException(){
        super("Email address is not verified.  Plaese verify your email .");
    }
}
