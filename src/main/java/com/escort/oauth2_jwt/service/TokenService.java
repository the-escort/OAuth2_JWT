package com.escort.oauth2_jwt.service;

import com.escort.oauth2_jwt.domain.entity.User;
import com.escort.oauth2_jwt.repository.RefreshTokenRepository;
import com.escort.oauth2_jwt.repository.UserRepository;
import com.escort.oauth2_jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createNewAccessToken(String refreshToken) {
        if (jwtUtil.validToken(refreshToken)) {
            Long userId = refreshTokenRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new IllegalArgumentException("Unexpected token"))
                    .getId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));

            return jwtUtil.generateToken(user, Duration.ofHours(2));
        } else {
            throw new IllegalArgumentException("Unexpected token");
        }
    }

}
