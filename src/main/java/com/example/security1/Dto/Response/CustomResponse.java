package com.example.security1.Dto.Response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomResponse  <T>   {
    private  boolean success;;
    private String message;
    private T data;
}
