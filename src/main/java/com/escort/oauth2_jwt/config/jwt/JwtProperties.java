package com.escort.oauth2_jwt.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties("jwt")
@Component
public class JwtProperties {

    private String issuer;

    private String secretKey;

}
