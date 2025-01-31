package com.teamproject.back.dto.oauth2;


import lombok.ToString;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;


@ToString
public class NaverUserDTO extends AbstractOAuth2User {
    public NaverUserDTO(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        super(clientRegistration, oAuth2User);
    }

    public String getEmail(){
        return Optional.ofNullable(oAuth2User.getAttributes())
            .map(attr -> (Map<String, Object>) attr.get("response"))
            .map(response -> (String) response.get("email"))
            .orElse(null);
    }

    public String getUsername(){
        return Optional.ofNullable(oAuth2User.getAttributes())
                .map(attr -> (Map<String, Object>) attr.get("response"))
                .map(response -> (String) response.get("name"))
                .orElse(null);
    }

}
