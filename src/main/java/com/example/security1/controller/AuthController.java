package com.example.security1.controller;


import com.example.security1.Dto.Request.*;
import com.example.security1.Dto.Response.AuthResponse;
import com.example.security1.Dto.Response.CustomResponse;
import com.example.security1.entity.RefreshToken;
import com.example.security1.service.EmailService;
import com.example.security1.exception.*;
import com.example.security1.service.RefreshTokenService;
import com.example.security1.util.CodeGenerator;
import com.example.security1.util.JwtUtil;
import com.example.security1.entity.User;
import com.example.security1.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
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

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;
    private final RestClient.Builder builder;
    private final EmailService emailService;


    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, RefreshTokenService refreshTokenService,
                          UserRepository userRepository, RestClient.Builder builder, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.builder = builder;
        this.emailService = emailService;
    }





    @PostMapping("/register")
    public ResponseEntity<CustomResponse<?>> register(@Valid  @RequestBody RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw  new UserAlreadyExistException("Error: Username is already taken");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw  new UserAlreadyExistException("Error: Email is already in use");
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




        emailService.senderVerificationMail(user.getEmail(),verificationCode);
        CustomResponse<String > response = CustomResponse.<String>builder()
                .success(true)
                .message("User registered successfully. Verification email sent.")
                .data(null)
                .build();

        return ResponseEntity.ok(response);





       // failed to send email exception handle in email service

    }


    @PostMapping("verify")
    public ResponseEntity<CustomResponse<?>> verifyEmail(@RequestBody VerifyRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        if (user.getVerified()) {
            throw  new EmailAlreadyVerifiedException();
        }

        if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            throw  new VerificationCodeExpiredException();
        }
        if (!user.getVerificationCode().equals(request.getCode())) {
           throw new InvalidVerificationException();
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
                .orElseThrow(() -> new UserNotFoundException());

         if (!user.getVerified()) {
             throw new EmailNotVerifiedException();
         }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );    // bad credentails yok otomatik yakalndı bu kısm



        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        final String accessToken = jwtUtil.generateToken(userDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .type("Bearer")
                .username(userDetails.getUsername())
                .build();

        CustomResponse<AuthResponse> response = CustomResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(authResponse)
                .build();

        return ResponseEntity.ok(response);
    }



    @PostMapping("/logout")
    public  ResponseEntity<CustomResponse<?>> logout(@RequestBody LogoutRequest request) {

        refreshTokenService.deleteByToken(request.getRefreshToken());


        CustomResponse<String> response = CustomResponse.<String>builder()
                .success(true)
                .message("Logout successful")
                .data(null)
                .build();

        return ResponseEntity.ok(response);



    }

    @PostMapping("/refresh")
    @Transactional
    public  ResponseEntity<CustomResponse<?>>  refreshToken(@RequestBody RefreshTokenRequest request) {

       RefreshToken oldRefreshToken = refreshTokenService.findByToken(request.getRefreshToken());

      oldRefreshToken =refreshTokenService.verifyExpirationDate(oldRefreshToken);


      User user=oldRefreshToken.getUser();

      UserDetails userDetails= userDetailsService.loadUserByUsername(user.getUsername());

      String newAccessToken= jwtUtil.generateToken(userDetails);



      // yeni refresh token (rotation)
      RefreshToken newRefreshToken= refreshTokenService.createRefreshToken( user.getUsername());


      refreshTokenService.deleteByToken(oldRefreshToken.getToken());

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .type("Bearer")
                .username(userDetails.getUsername())
                .build();

        CustomResponse<AuthResponse> response = CustomResponse.<AuthResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(authResponse)
                .build();

        return ResponseEntity.ok(response);














    }




}
