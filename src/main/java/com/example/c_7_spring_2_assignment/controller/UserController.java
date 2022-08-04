package com.example.c_7_spring_2_assignment.controller;

import com.example.c_7_spring_2_assignment.dto.TokenDto;
import com.example.c_7_spring_2_assignment.dto.UserSignupDto;
import com.example.c_7_spring_2_assignment.sercurity.UserDetailsImpl;
import com.example.c_7_spring_2_assignment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/members")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    //회원가입(모두 접근가능)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupDto userSignupDto) {
        return userService.join(userSignupDto);
    }

    //access token 유효기간 만료시 재발급
    @PostMapping("/refresh-tokens")
    public ResponseEntity<?> issueTokens(@RequestBody TokenDto tokenDto) {
        return userService.reissueTokens(tokenDto);
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.logout(userDetails);
    }


}
