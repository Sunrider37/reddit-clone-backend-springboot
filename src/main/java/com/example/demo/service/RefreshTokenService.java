package com.example.demo.service;

import com.example.demo.exception.SpringRedditException;
import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }
    void validateRefreshToken(String token) throws SpringRedditException {
        refreshTokenRepository.findByToken(token).orElseThrow(()-> new SpringRedditException("Invalid refresh token"));
    }
    public void deleteRefreshToken(String token){
        refreshTokenRepository.deleteByToken(token);
    }
}
