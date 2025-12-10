package ru.project.jbd.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import ru.project.jbd.exception.TokenRefreshException;
import ru.project.jbd.repository.RefreshTokenRepository;
import ru.project.jbd.security.jwt.RefreshToken;

@Service
@Log4j2
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(UserDetailsImpl userDetails) {
        log.info("#createRefreshToken user = " + userDetails.toString());

        if(refreshTokenRepository.findById(userDetails.getId()).isPresent()) return refreshTokenRepository.findById(userDetails.getId()).get();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(userDetails.getId());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        log.info("#createRefreshToken created refreshToken  = " + refreshToken.toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
        refreshTokenRepository.delete(token);
        throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
