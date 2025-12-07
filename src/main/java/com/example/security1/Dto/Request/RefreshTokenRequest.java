package com.example.security1.Dto.Request;

import lombok.Data;

import javax.naming.ldap.PagedResultsControl;

@Data
public class RefreshTokenRequest {

    private String refreshToken;
}
