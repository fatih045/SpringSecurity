package com.example.security1.controller;


import com.example.security1.Dto.Response.CustomResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.PanelUI;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public ResponseEntity<CustomResponse<String>> publicMethod() {
        CustomResponse<String> response = CustomResponse.<String>builder()
                .success(true)
                .message("Public endpoint accessed successfully.")
                .data("This is a public endpoint.")
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CustomResponse<String>> userEndpoint() {

        CustomResponse<String> response = CustomResponse.<String>builder()
                .success(true)
                .message("User endpoint result")
                .data("Hello User!")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomResponse<String>> adminEndpoint() {

        CustomResponse<String> response = CustomResponse.<String>builder()
                .success(true)
                .message("Admin endpoint result")
                .data("Hello Admin!")
                .build();

        return ResponseEntity.ok(response);
    }
}
