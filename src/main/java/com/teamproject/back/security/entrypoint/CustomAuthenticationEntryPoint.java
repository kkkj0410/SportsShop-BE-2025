package com.teamproject.back.security.entrypoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        // 인증 실패 시 특정 URL로 리다이렉트
        // 인증 실패 시, 기본적으로 /login으로 응답
        // 해당 응답은 response.ok라 프론트에게 혼란을 야기
        // 따라서 unAuthorized로 응답하여 응답을 안해버림

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

    }
}