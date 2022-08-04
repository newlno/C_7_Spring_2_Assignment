package com.example.c_7_spring_2_assignment.sercurity.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.c_7_spring_2_assignment.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Setter
@Component
@RequiredArgsConstructor
//JWT Token 생성
public class JwtTokenUtils {

    private static final int ACCESS_TOKEN_EXPIRE_TIME = 1000 * 30; // 30초
    private static final int REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 5; // 5분


    // Access Token 생성
    public static String generateACJwtToken(UserDetailsImpl userDetails) {
        return JWT.create()
                .withIssuer("kelly")
                .withClaim("EXPIRATION_TIME", new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRE_TIME))
                .withClaim("USERNAME", userDetails.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET_KEY));
    }

    // Refresh Token 생성
    public static String generateREJwtToken(UserDetailsImpl userDetails) {

        return JWT.create()
                .withIssuer("kelly")
                .withClaim("USERNAME", userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET_KEY));
    }
}
