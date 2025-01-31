package com.teamproject.back.service.converter;

import com.teamproject.back.dto.oauth2.GoogleUserDTO;
import com.teamproject.back.dto.oauth2.ProviderUser;
import com.teamproject.back.dto.oauth2.SocialType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleConverter implements Converter{

    public ProviderUser converter(ClientRegistration clientRegistration, OAuth2User oAuth2User){
        GoogleUserDTO googleUserDTO = new GoogleUserDTO(clientRegistration, oAuth2User);

        if(googleUserDTO.getProvider().equals(SocialType.GOOGLE.getName())){
            return googleUserDTO;
        }

        return null;
    }
}
