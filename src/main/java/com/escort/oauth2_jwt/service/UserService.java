package com.escort.oauth2_jwt.service;

import com.escort.oauth2_jwt.constant.Role;
import com.escort.oauth2_jwt.domain.dto.UserDto;
import com.escort.oauth2_jwt.domain.entity.User;
import com.escort.oauth2_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .map(u -> u.setName(name))
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .role(Role.USER)
                        .build());
        userRepository.save(user);
        UserDto userDto = UserDto.fromEntity(user);
        userDto.setAttributes(attributes);

        return userDto;
    }

}
