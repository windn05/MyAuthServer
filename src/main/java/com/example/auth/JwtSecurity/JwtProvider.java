package com.example.auth.JwtSecurity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long validityInMs;

    private SecretKey signingKey;

    @PostConstruct
    public void init(){
        signingKey = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 토큰 생성
    public String createToken(String userid) {
        long now = (new Date()).getTime();
        Date expiration = new Date(now + 3600000); // 24시간 후 만료

        return Jwts.builder()
                .setSubject(userid) // 토큰 주체(사용자 아이디)
                .setIssuedAt(new Date()) // 토큰 발행 시간
                .setExpiration(expiration) // 토큰 만료 시간
                .signWith(signingKey, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUserid(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
