package com.example.c_7_spring_2_assignment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserLoginDto {
    private String username;
    private String password;
}
