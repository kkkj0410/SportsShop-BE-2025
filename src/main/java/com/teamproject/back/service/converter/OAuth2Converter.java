package com.teamproject.back.service.converter;

import com.teamproject.back.dto.oauth2.ProviderUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OAuth2Converter implements Converter{

    private final GoogleConverter googleConverter;
    private final NaverConverter naverConverter;

    @Autowired
    public OAuth2Converter(GoogleConverter googleConverter, NaverConverter naverConverter) {
        this.googleConverter = googleConverter;
        this.naverConverter = naverConverter;
    }

    public ProviderUser converter(ClientRegistration clientRegistration, OAuth2User oAuth2User){
        List<Converter> converterList = fetchConverterList();


        return converterList.stream()
                .map(converter -> converter.converter(clientRegistration, oAuth2User))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private List<Converter> fetchConverterList(){
        return List.of(googleConverter, naverConverter);
    }



}
