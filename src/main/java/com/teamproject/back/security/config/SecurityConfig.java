package com.teamproject.back.security.config;

import com.teamproject.back.jwt.JwtAuthenticationFilter;
import com.teamproject.back.security.entrypoint.CustomAuthenticationEntryPoint;
import com.teamproject.back.security.handler.OAuth2SuccessHandler;
import com.teamproject.back.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Profile("local")
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2Service oAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, OAuth2Service oAuth2Service, OAuth2SuccessHandler oAuth2SuccessHandler, CustomAuthenticationEntryPoint customAuthenticationEntryPoint){
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.oAuth2Service = oAuth2Service;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors
                        .configurationSource(CorsConfig.corsConfigurationSource())// CORS 허용
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/api/auth/**"),
                                new AntPathRequestMatcher("/signup"),
                                new AntPathRequestMatcher("/ws/chat/**"),
                                new AntPathRequestMatcher("/api/signup"),
                                new AntPathRequestMatcher("/image"),
                                new AntPathRequestMatcher("/api/category"),
                                new AntPathRequestMatcher("/api/header"),
                                new AntPathRequestMatcher("/api/chat/**"),
                                new AntPathRequestMatcher("/api/home"),
                                new AntPathRequestMatcher("/ws/chat/**"),
                                new AntPathRequestMatcher("/api/signup"),
                                new AntPathRequestMatcher("/image"),
                                new AntPathRequestMatcher("/api/email/**"),
                                new AntPathRequestMatcher("/api/user/{email}"),
                                new AntPathRequestMatcher("/api/login/**"),
                                new AntPathRequestMatcher("/api/search/**"),
                                new AntPathRequestMatcher("/api/login/**")

                        ).permitAll()
                        .requestMatchers("/security/user").hasRole("USER")
                        .requestMatchers("/security/admin").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/login/oauth2/redirect/**").denyAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handle -> handle
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfoEndPointConfig -> userInfoEndPointConfig
                                .userService(oAuth2Service)
                        )
                        .successHandler(oAuth2SuccessHandler)
                );


        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}