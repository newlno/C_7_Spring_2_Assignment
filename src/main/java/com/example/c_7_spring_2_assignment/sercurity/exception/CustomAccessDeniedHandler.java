package com.example.c_7_spring_2_assignment.sercurity.exception;

import com.example.c_7_spring_2_assignment.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());


        ResponseDto<?> responseDto = new ResponseDto<>(false, null,
                new ResponseDto.Error("403 FORBIDDEN", "접근권한이 없습니다."));
        objectMapper.writeValue(response.getOutputStream(), responseDto);
    }
}
