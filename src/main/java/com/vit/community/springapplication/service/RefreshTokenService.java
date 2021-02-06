package com.vit.community.springapplication.service;

import com.vit.community.springapplication.exceptions.SpringCommunityException;
import com.vit.community.springapplication.model.RefreshToken;
import com.vit.community.springapplication.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/*
* The RefreshTokenService class is responsible for generating and validating refresh tokens
* used for generating new JSON Web Tokens when they expire.
* */

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    /* Generates a Refresh token for users when they log in */

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    /* Validates the refresh token before a new JWT is generated for the user */

    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringCommunityException("Invalid refresh Token"));
    }

    /* Deletes the user's refresh token from the database when they log out. */

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
