package com.example.security1.repository;


import com.example.security1.entity.RefreshToken;
import com.example.security1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);    // log out yaparken kullanacağız

    void  deleteByToken(String token);
}
