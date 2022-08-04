package com.example.c_7_spring_2_assignment.sercurity.filter;

/*
 Client는 이제부터 Header에 JWT 토큰을 넣어서 요청을 줘야하고
 유효성을 확인 후에 서버는 응답을해준다
 */

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.c_7_spring_2_assignment.entity.User;
import com.example.c_7_spring_2_assignment.repository.UserRepository;
import com.example.c_7_spring_2_assignment.sercurity.UserDetailsImpl;
import com.example.c_7_spring_2_assignment.sercurity.jwt.JwtProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/*
    API요청 Header에 전달되는 JWT토큰 유효성 검사(인증)
 */
public class JwtAuthFilter extends BasicAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

    public JwtAuthFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        //request Header에 Token값 확인
        String jwtInHeader = request.getHeader(JwtProperties.AUTH_HEADER);

        //Header에 Token이 존재하는지 확인 -> 없으면 filter chain으로 리턴
        if(jwtInHeader == null || !jwtInHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request,response);
            return;
        }

        // Bearer 제외한 token정보만 추출
        String jwtToken = jwtInHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        // Algorithm과 secret key를 제공하여 복호화하는 메소드실행
        DecodedJWT decodedJWT = decodeJwt(jwtToken);

        String username = decodedJWT.getClaim("USERNAME").asString();
        Date expireDate = decodedJWT.getClaim("EXPIRATION_TIME").asDate();
        Date now = new Date();

        //access token 유효기간 만료확인 -> 만료시 filter chain으로 리턴
        if(expireDate.before(now)) {
            chain.doFilter(request,response);
            return;
        }

        // 정상적으로 서명이 된 경우
        if(username != null) {
            User userEntity = userRepository.findByUsername(username);
            UserDetailsImpl userDetails = new UserDetailsImpl(userEntity);

            //강제로 authentication을 만들어 로그인을 시켜줘야함
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null);

            // 강제로 security의 session에 접근하여 authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication); // 저장할 수 있는 session공간을 찾음

            chain.doFilter(request, response);
        }
    }

    public DecodedJWT decodeJwt(String jwtInHeader) {
        // Bearer 제외한 token정보만 추출
        String jwtToken = jwtInHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        DecodedJWT jwt = null;

        try{
            Algorithm algorithm = Algorithm.HMAC512(JwtProperties.SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();

            jwt = verifier.verify(jwtToken);
        } catch (Exception e) {
            throw new RuntimeException("유효한 토큰이 아닙니다.");
        }
        return jwt;
    }

}
