package com.example.security1.Dto.Request;

import lombok.Data;

@Data
public class LogoutRequest {

    private String refreshToken;
}
