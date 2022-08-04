package com.example.c_7_spring_2_assignment.sercurity.exception;

import com.example.c_7_spring_2_assignment.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 필터에서 401 Error 발생시, Response!
// 토큰이 만료되거나 header에 토큰이 없을시 발생하는 Exception
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());


        ResponseDto<?> responseDto = new ResponseDto<>(false, null,
                new ResponseDto.Error("401 UNAUTHORIZED", "토큰 인증에 실패하였습니다."));
        objectMapper.writeValue(response.getOutputStream(), responseDto);

    }
}
