package com.example.security1.exception;

public class UserNotFoundException  extends  RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }


    public UserNotFoundException()     // Default constructor with a default message
    {
        super("User not found");
    }

}
