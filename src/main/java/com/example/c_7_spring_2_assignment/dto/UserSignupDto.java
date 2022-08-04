package com.example.c_7_spring_2_assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupDto {
    private String username;
    private String password;
    private String passwordConfirm;
}
