package com.example.security1.controller;


import com.example.security1.Dto.AuthResponse;
import com.example.security1.Dto.LoginRequest;
import com.example.security1.Dto.RegisterRequest;
import com.example.security1.Dto.Request.VerifyRequest;
import com.example.security1.Dto.Response.CustomResponse;
import com.example.security1.EmailService;
import com.example.security1.util.CodeGenerator;
import com.example.security1.util.JwtUtil;
import com.example.security1.User;
import com.example.security1.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private  final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private  final PasswordEncoder passwordEncoder;

    private  final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final RestClient.Builder builder;
    private final EmailService emailService;


    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          UserRepository userRepository, RestClient.Builder builder, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.builder = builder;
        this.emailService = emailService;
    }





    @PostMapping("/register")
    public ResponseEntity<CustomResponse<?>> register(@RequestBody RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(false)
                    .message("Error: Username is already taken")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(false)
                    .message("Error: Email is already in use")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        if (registerRequest.getRoles() == null || registerRequest.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>());
            user.getRoles().add("ROLE_USER");
        } else {
            user.setRoles(registerRequest.getRoles());
        }


        String verificationCode = CodeGenerator.generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));
        user.setVerified(false);

        userRepository.save(user);





        try {
            emailService.senderVerificationMail(user.getEmail(), verificationCode);
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(true)
                    .message("User registered successfully. Verification email sent.")
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(false)
                    .message("User registered but failed to send verification email: " + e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.ok(response);
        }

    }


    @PostMapping("verify")
    public ResponseEntity<CustomResponse<?>> verifyEmail(@RequestBody VerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (user.getVerified()) {
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(false)
                    .message("Error: Invalid verification code")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(false)
                    .message("Error: Verification code has expired")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }
        if (!user.getVerificationCode().equals(request.getCode())) {
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(false)
                    .message("Error: Invalid verification code")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);

        CustomResponse<String> response = CustomResponse.<String>builder()
                .success(true)
                .message("Email verified successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }




    @PostMapping("/login")
    public ResponseEntity<CustomResponse<?>> login(@RequestBody LoginRequest loginRequest) {

         User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found!"));

         if (!user.getVerified()) {
             CustomResponse<String> response = CustomResponse.<String>builder()
                     .success(false)
                     .message("Error: Email is not verified")
                     .data(null)
                     .build();
             return ResponseEntity.badRequest().body(response);
         }


        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            CustomResponse<String> response = CustomResponse.<String>builder()
                    .success(false)
                    .message("Error: Invalid username or password")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(response);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        AuthResponse authResponse = new AuthResponse(jwt, loginRequest.getUsername());

        CustomResponse<AuthResponse> response = CustomResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .build();

        return ResponseEntity.ok(response);
    }




}
