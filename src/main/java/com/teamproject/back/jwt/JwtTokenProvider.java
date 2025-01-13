package com.teamproject.back.jwt;


import com.teamproject.back.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Component
public class JwtTokenProvider {


    @Value("${jwt.key}")
    private String KEY;
    private SecretKey SECRET_KEY;
    private final long ACCESS_EXPIRE_TIME = 1000 * 60 * 12 * 30L;
    private final long REFRESH_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;
    private final String KEY_ROLE = "role";
    private final String HEADER_TYPE = "typ";
    private final String HEADER_JWT_TYPE = "JWT";
    private final String ALGORITHM = "HmacSHA256";
    private final String JWT_COOKIE_NAME = "JwtCookie";

    @PostConstruct
    private void setSecretKey() {
        if(KEY.length() < 32){
            log.info("JWT 키의 문자열 길이는 32 이상이어야 합니다.");
            throw new IllegalArgumentException("The secret key must be at least 32 bytes long.");
        }

        SECRET_KEY = Keys.hmacShaKeyFor(KEY.getBytes());
    }

    public Cookie createJwtCookie(String email, Role role){
        String jwtToken = createToken(email, role);
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, jwtToken);
        cookie.setHttpOnly(false);  // 클라이언트에서 JavaScript로 접근 불가
//        cookie.setSecure(true);    // HTTPS에서만 전송
        cookie.setPath("/");       // 모든 경로에서 쿠키에 접근 가능
        cookie.setMaxAge((int)ACCESS_EXPIRE_TIME);

        return cookie;
    }

    public String createToken(String email, Role role) {
        Date beginDate = new Date();
        Date endDate = new Date(beginDate.getTime() + ACCESS_EXPIRE_TIME);

        return Jwts.builder()
                .header()
                .add(HEADER_TYPE, HEADER_JWT_TYPE)
                .and()
                .subject(email)
                .claim(KEY_ROLE, "ROLE_"+role)
                .issuedAt(beginDate)
                .expiration(endDate)
                .signWith(new SecretKeySpec(SECRET_KEY.getEncoded(), ALGORITHM))
                .compact();
    }

    public String createToken(Authentication authentication) {
        Date beginDate = new Date();
        Date endDate = new Date(beginDate.getTime() + ACCESS_EXPIRE_TIME);

        //authentication.getName() is username
        log.info("authentication.getName() : {}", authentication.getName());

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder()
                .header()
                .add(HEADER_TYPE, HEADER_JWT_TYPE)
                .and()
                .subject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .issuedAt(beginDate)
                .expiration(endDate)
                .signWith(new SecretKeySpec(SECRET_KEY.getEncoded(), ALGORITHM))
                .compact();
    }

    public boolean isValid(String token) {

        Claims payload = tokenToClaims(token);

        try{
            if(payload != null && payload.getExpiration().after(new Date())){
                return true;
            }
        }catch(NullPointerException e){
            log.error("payload || getExpiration() is null");
            return false;
        }

        return false;
    }


    public boolean validateToken(String token) {
        try {

            Claims payload = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = tokenToClaims(token);
        if(claims == null){
            return null;
        }
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);
        log.info("authorities : {}", authorities);

        User principal = new User(claims.getSubject(), "",authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get(KEY_ROLE).toString()));
    }

    private Claims tokenToClaims(String token){
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }



}
