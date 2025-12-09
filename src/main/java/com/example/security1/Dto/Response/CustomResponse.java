package com.example.security1.Dto.Response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null alanları json a  ekleme
public class CustomResponse  <T>   {
    private  boolean success;;
    private String message;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    //  hata detaytları için ek alan
    private  String path;
    private Integer status;
}
