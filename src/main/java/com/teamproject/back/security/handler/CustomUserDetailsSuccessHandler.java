package com.teamproject.back.security.handler;

import com.teamproject.back.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@Slf4j
public class CustomUserDetailsSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String URI = "/login/success";
    private int TIME = 60*60;

    @Autowired
    public CustomUserDetailsSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String token = jwtTokenProvider.createToken(authentication);

        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .build().toUriString();

        log.info("token : {}", token);

        Cookie cookie = createCookie(token, TIME);

        response.addCookie(cookie);
        response.sendRedirect(redirectUrl);
    }

    private Cookie createCookie(String token, int time){
        Cookie cookie = new Cookie("JwtToken", token);
        cookie.setHttpOnly(true);  // 쿠키 숨기기
        cookie.setSecure(true);    // HTTPS 에서만 전송
        cookie.setMaxAge(time);

        return cookie;
    }
}
