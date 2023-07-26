package com.escort.oauth2_jwt.controller;

import com.escort.oauth2_jwt.controller.request.AccessTokenRequest;
import com.escort.oauth2_jwt.controller.response.AccessTokenResponse;
import com.escort.oauth2_jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<AccessTokenResponse> createNewAccessToken(@RequestBody AccessTokenRequest accessTokenRequest) {
        String newAccessToken = tokenService.createNewAccessToken(accessTokenRequest.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccessTokenResponse(newAccessToken));
    }

}
