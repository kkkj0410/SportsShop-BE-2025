package com.teamproject.back.dto.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface ProviderUser extends OAuth2User {

    String getEmail();
    String getUsername();
    String getProvider();
}
