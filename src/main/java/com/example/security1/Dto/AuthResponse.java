package com.example.security1.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private  String token ;
    private  String type ="Bearer";
    private  String username;

    public  AuthResponse(String token, String username){
        this.token=token;
        this.username=username;
    }
}
