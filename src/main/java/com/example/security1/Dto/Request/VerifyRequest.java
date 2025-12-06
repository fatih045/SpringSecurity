package com.example.security1.Dto.Request;

import lombok.Data;

@Data
public class VerifyRequest {


    private  String email;
    private String code;

}
