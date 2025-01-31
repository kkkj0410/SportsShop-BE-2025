package com.teamproject.back.security.handler;

import com.teamproject.back.dto.oauth2.GoogleUserDTO;
import com.teamproject.back.dto.oauth2.ProviderUser;
import com.teamproject.back.entity.Users;
import com.teamproject.back.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    private final String failUrl = "/api/login/oauth2/redirect/fail";
    private final String successUrl = "/api/login/oauth2/redirect/success";

    @Autowired
    public OAuth2SuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        ProviderUser providerUser = (ProviderUser)authentication.getPrincipal();

        if(userRepository.findByEmail(providerUser.getEmail()) == null){
            log.info("회원가입 리다이렉트 : {}", providerUser.getEmail());
            redirectSignup(response, providerUser.getEmail());
            return;
        }

        redirectLogin(response, providerUser.getEmail());
    }


    public Users findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    private void redirectSignup(HttpServletResponse response, String email){
        try {
            response.sendRedirect(failUrl + "/" + email);
        } catch (IOException e) {
            log.error("리다이렉트 실패", e);
        }
    }

    private void redirectLogin(HttpServletResponse response, String email){
        try {
            response.sendRedirect(successUrl + "/" + email);
        } catch (IOException e) {
            log.error("리다이렉트 실패", e);
        }
    }

}
