package com.teamproject.back.dto.oauth2;


import lombok.ToString;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

@ToString
public class GoogleUserDTO extends AbstractOAuth2User {

    public GoogleUserDTO(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        super(clientRegistration, oAuth2User);
    }


    public String getEmail(){
        return (String) oAuth2User.getAttributes().get("email");
    }

    public String getUsername(){
        return (String) oAuth2User.getAttributes().get("name");
    }

}
