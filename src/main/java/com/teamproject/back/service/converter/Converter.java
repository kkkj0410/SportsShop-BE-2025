package com.teamproject.back.service.converter;

import com.teamproject.back.dto.oauth2.ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface Converter {


    ProviderUser converter(ClientRegistration clientRegistration, OAuth2User oAuth2User);
}
