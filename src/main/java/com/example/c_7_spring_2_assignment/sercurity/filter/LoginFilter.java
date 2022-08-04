package com.example.c_7_spring_2_assignment.sercurity.filter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    JSON형태로 HttpServletRequest를 통해 들어온 아이디와 패스워드 정보를 넣어
    UsernamePaswwordAuthenticationToken을 생성.

    -> 1) 이 Filter를 통해 필요한 정보를 적합한 클래스 형태(Authentication)로 만들어
       2) Spring Security에 인증 요청
 */
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    final ObjectMapper objectMapper;

    public LoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        objectMapper = new ObjectMapper()
                // 선언되지 않은 모든 속성 무시
                // JSON 에서 몇 가지 속성을 찾고 전체 매핑을 작성하지 않으려는 경우
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken;

        try {
            JsonNode requestBody = objectMapper.readTree(request.getInputStream());
            String username = requestBody.get("username").asText();
            String password = requestBody.get("password").asText();

            // 1) JSON객체의 데이터를 Authentication으로 만듬 for 인증
            authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        } catch (IOException e) {
            throw new RuntimeException("username, password 입력이 필요합니다.(JSON)");
        }

        setDetails(request, authenticationToken);

        // 2) 생성한 authenticationToken 인증요청
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }


}
