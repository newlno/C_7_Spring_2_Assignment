package com.example.c_7_spring_2_assignment.sercurity;

import com.example.c_7_spring_2_assignment.dto.ResponseDto;
import com.example.c_7_spring_2_assignment.entity.User;
import com.example.c_7_spring_2_assignment.repository.UserRepository;
import com.example.c_7_spring_2_assignment.sercurity.jwt.JwtProperties;
import com.example.c_7_spring_2_assignment.sercurity.jwt.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    Login 성공시에 호출되는 클래스
    -> 로그인이 되었으니 JWT Token 생성해주는 역할
 */
@NoArgsConstructor
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    public LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        // authentication.getPrincipal() 실행하면 UserDetails를 구현한 사용자 객체를 반환
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        //AccessToken 생성
        final String accessJwtToken = JwtTokenUtils.generateACJwtToken(userDetails);
        //RefreshToken 생성 -> DB에 저장
        final String refreshJwtToken = JwtTokenUtils.generateREJwtToken(userDetails);


        // Response Header에 access, refresh 토큰 담아줌
        response.addHeader(JwtProperties.AUTH_HEADER, JwtProperties.TOKEN_PREFIX + accessJwtToken);
        response.addHeader(JwtProperties.REFRESH_HEADER, JwtProperties.TOKEN_PREFIX + refreshJwtToken);

        User user = userDetails.getUser();
        //refresh Token을 User DB에 넣어주기
        user.setRefreshToken(refreshJwtToken);
        userRepository.save(user);

        // ------------------------------Response -------------------------------------
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ResponseDto<User> responseDto = new ResponseDto<>(true, user, null);
        String result = objectMapper.writeValueAsString(responseDto);
        response.getWriter().write(result);
    }



}
