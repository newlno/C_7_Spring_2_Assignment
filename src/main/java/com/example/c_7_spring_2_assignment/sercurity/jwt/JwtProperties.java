package com.example.c_7_spring_2_assignment.sercurity.jwt;

public interface JwtProperties {
    String SECRET_KEY = "kelly"; // 우리서버만 알고 있는 비밀값
    String TOKEN_PREFIX = "BEARER ";
    String AUTH_HEADER = "Authorization"; // Access Token 헤더 key값
    String REFRESH_HEADER = "Refresh-Token"; // Refresh Token 헤더 key값
}
