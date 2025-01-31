package com.teamproject.back.service;


import com.teamproject.back.dto.oauth2.ProviderUser;
import com.teamproject.back.entity.Role;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.UserRepository;
import com.teamproject.back.service.converter.OAuth2Converter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;


//OAuth2UserService<OAuth2UserRequest, OAuth2User>
@Service
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {

//    private final UserRepository userRepository;
    private final OAuth2Converter oAuth2Converter;
//    private final HttpServletResponse response;

//    private final String signupUrl = "/api/login/oauth2/redirect/email";
//    private final String loginUrl = "/api/login/oauth2/redirect";

//    @Autowired
//    public OAuth2Service(UserRepository userRepository, OAuth2Converter oAuth2Converter, HttpServletResponse response) {
//        this.userRepository = userRepository;
//        this.oAuth2Converter = oAuth2Converter;
//        this.response = response;
//    }

    @Autowired
    public OAuth2Service(OAuth2Converter oAuth2Converter){
        this.oAuth2Converter = oAuth2Converter;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ProviderUser providerUser = oAuth2Converter.converter(clientRegistration, oAuth2User);

//        if(findByEmail(providerUser.getEmail()) == null){
//            log.info("OAuth2 회원 저장 : {}", providerUser);
//            save(toEntity(providerUser));
//            redirectSignup(providerUser.getEmail());
//        }

        return providerUser;
    }

//    public Users findByEmail(String email){
//        return userRepository.findByEmail(email);
//    }
//
//    public Users save(Users user){
//        return userRepository.save(user);
//    }

//    private void redirectSignup(String email){
//        try {
//            response.sendRedirect(signupUrl + "?" + email);
//        } catch (IOException e) {
//            log.error("리다이렉트 실패", e);
//        }
//    }
//
//    private void redirectLogin(){
//        try {
//            response.sendRedirect(loginUrl);
//        } catch (IOException e) {
//            log.error("리다이렉트 실패", e);
//        }
//    }

//    private Users toEntity(ProviderUser providerUser){
//        return Users.builder()
//                .email(providerUser.getEmail())
//                .username(providerUser.getUsername())
//                .role(Role.USER)
//                .build();
//    }

}
