package com.example.security1.service;


import com.example.security1.entity.RefreshToken;
import com.example.security1.entity.User;
import com.example.security1.exception.RefreshTokenExpiredException;
import com.example.security1.exception.RefreshTokenNotFoundException;
import com.example.security1.exception.UserNotFoundException;
import com.example.security1.repository.RefreshTokenRepository;
import com.example.security1.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.swing.plaf.PanelUI;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService   {

    private  final RefreshTokenRepository refreshTokenRepository;

    private  final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }






    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UserNotFoundException());


        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();


        return  refreshTokenRepository.save(refreshToken);
    }




    public  RefreshToken verifyExpirationDate(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            new RefreshTokenExpiredException("Refresh token has expired. Please login again.");
        }
        return token;
    }




    public  RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException());
    }

    public  void  deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public  void  deleteByToken(String token) {
        RefreshToken refreshToken=refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException());

        refreshTokenRepository.delete(refreshToken);
    }
}
