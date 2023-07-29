package com.escort.oauth2_jwt.config;

import com.escort.oauth2_jwt.domain.dto.UserDto;
import com.escort.oauth2_jwt.domain.entity.User;
import com.escort.oauth2_jwt.repository.UserRepository;
import com.escort.oauth2_jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        if (jwtUtil.validToken(token)) {
            User user = userRepository.findByEmail(jwtUtil.getEmail(token))
                    .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
            UserDto userDto = UserDto.fromEntity(user);
            UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDto, token, userDto.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.split(" ")[1].trim();
        }

        return null;
    }

}
