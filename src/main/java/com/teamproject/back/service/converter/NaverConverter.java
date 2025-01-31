package com.teamproject.back.service.converter;

import com.teamproject.back.dto.oauth2.NaverUserDTO;
import com.teamproject.back.dto.oauth2.ProviderUser;
import com.teamproject.back.dto.oauth2.SocialType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class NaverConverter implements Converter{

    public ProviderUser converter(ClientRegistration clientRegistration, OAuth2User oAuth2User){
        NaverUserDTO naverUserDTO = new NaverUserDTO(clientRegistration, oAuth2User);

        if(naverUserDTO.getProvider().equals(SocialType.NAVER.getName())){
            return naverUserDTO;
        }

        return null;
    }

}
