package com.escort.oauth2_jwt.config.oauth;

import com.escort.oauth2_jwt.domain.entity.RefreshToken;
import com.escort.oauth2_jwt.domain.entity.User;
import com.escort.oauth2_jwt.repository.RefreshTokenRepository;
import com.escort.oauth2_jwt.repository.UserRepository;
import com.escort.oauth2_jwt.util.CookieUtil;
import com.escort.oauth2_jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
public class OAUth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = userRepository.findByEmail((String) oAuth2User.getAttributes().get("email"))
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
        String refreshToken = jwtUtil.generateToken(user, Duration.ofDays(14));

        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        String accessToken = jwtUtil.generateToken(user, Duration.ofDays(1));
        String targetUrl = getTargetUrl(accessToken);

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(rt -> rt.setRefreshToken(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) Duration.ofDays(14).toSeconds();

        cookieUtil.deleteCookie(request, response, "refresh_token");
        cookieUtil.addCookie(response, "refresh_token", refreshToken, cookieMaxAge);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequest(request, response);
    }

    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString("/article")
                .queryParam("token", token)
                .build()
                .toUriString();
    }

}
