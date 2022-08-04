package com.example.c_7_spring_2_assignment.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.c_7_spring_2_assignment.dto.ResponseDto;
import com.example.c_7_spring_2_assignment.dto.TokenDto;
import com.example.c_7_spring_2_assignment.dto.UserLoginDto;
import com.example.c_7_spring_2_assignment.dto.UserSignupDto;
import com.example.c_7_spring_2_assignment.entity.User;
import com.example.c_7_spring_2_assignment.repository.UserRepository;
import com.example.c_7_spring_2_assignment.sercurity.UserDetailsImpl;
import com.example.c_7_spring_2_assignment.sercurity.jwt.JwtProperties;
import com.example.c_7_spring_2_assignment.sercurity.jwt.JwtTokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;


    //회원가입
    @Transactional
    public ResponseEntity<?> join(UserSignupDto userSignupDto) {
        String username = userSignupDto.getUsername();
        UserLoginDto userLoginDto = new UserLoginDto();


        // username 중복체크
        if(!usernameDupCheck(username)){
            return new ResponseEntity<>(ResponseDto.fail("EXIST NICKNAME", "중복된 닉네임입니다."),HttpStatus.OK);
        }

        // username validation check
        if (checkUsername(username)) {
            String password = userSignupDto.getPassword();
            boolean pwLen = checkPassword(password);
            String passwordConfirm = userSignupDto.getPasswordConfirm();

            if(pwLen && password.equals(passwordConfirm)) {
                userLoginDto.setPassword(passwordEncoder.encode(password));
                userLoginDto.setUsername(userSignupDto.getUsername());
                User saveUser = userRepository.save(new User(userLoginDto));
                return new ResponseEntity<>(ResponseDto.success(saveUser), HttpStatus.OK);
            } else if(!password.equals(passwordConfirm)){
                return new ResponseEntity<>(ResponseDto.fail("PASSWORD MISMATCH",
                        "비밀번호와 비밀번호확인이 일치하지 않습니다."),HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ResponseDto.fail("PASSWORD WRONG FORMAT",
                        "4-32자의 영문 소문자, 숫자만 사용 가능합니다."),HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(ResponseDto.fail("NICKNAME WRONG FORMAT",
                "4-12자의 영문 대문자,소문자, 숫자만 사용 가능합니다."),HttpStatus.OK);
    }


    // username 중복체크 method
    private boolean usernameDupCheck(String username) {
        User foundUser = userRepository.findByUsername(username);
        return foundUser == null;
    }

    // username validation
    private boolean checkUsername(String username) {
        /*
        닉네임 체크
        - 최소 4자이상, 최대 12자 이하
        - 알파벳 대소문자, 숫자로만 구성
         */
        char[] chList = username.toCharArray();
        if(chList.length < 4 || chList.length > 12) {
            return false;
        }

        for(char ch : chList) {
            if(!((ch >= 'a' && ch <= 'z') ||
                    (ch >= 'A' && ch <= 'Z') ||
                    (ch >= '0' && ch <= '9'))) {
                return false;
            }
        }
        return true;
    }

    // password validation check method
    private boolean checkPassword(String password) {
        char[] chList = password.toCharArray();
        if(chList.length < 4 || chList.length > 32) {
            return false;
        }

        for(char ch : chList) {
            if(!((ch >= 'a' && ch <= 'z') ||
                    (ch >= '0' && ch <= '9'))) {
                return false;
            }
        }
        return true;
    }


    @Transactional
    public ResponseEntity<?> reissueTokens(TokenDto tokenDto) {
        // Token Prefix 제거
        String accessToken = tokenDto.getAccessToken().substring(7);
        String refreshToken = tokenDto.getRefreshToken().substring(7);

        // access token 디코드해서 username 확인
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET_KEY)).build();
        DecodedJWT verify = verifier.verify(accessToken);
        String username = verify.getClaim("USERNAME").asString();

        // userRepository에서 User정보 찾고, refresh Token 비교
        User foundUser = userRepository.findByUsername(username);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        if (Objects.equals(foundUser.getRefreshToken(), refreshToken)) {
            TokenDto responseTokenDto = new TokenDto();
            responseTokenDto.setAccessToken(JwtProperties.TOKEN_PREFIX + JwtTokenUtils.generateACJwtToken(userDetails));
            responseTokenDto.setRefreshToken(JwtProperties.TOKEN_PREFIX + JwtTokenUtils.generateREJwtToken(userDetails));

            //refresh Token은 새로 발급한 것으로 DB데이터 수정
            foundUser.setRefreshToken(refreshToken);
            return new ResponseEntity<>(ResponseDto.success(responseTokenDto), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseDto.fail("INVALID TOKEN", "유효하지 않은 토큰입니다. 다시 로그인해주세요."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    public ResponseEntity<?> logout(UserDetailsImpl userDetails) {
        User logoutUser = userDetails.getUser();
        logoutUser.setRefreshToken(null);
        return new ResponseEntity<>(ResponseDto.success("로그아웃되었습니다."), HttpStatus.OK);
    }
}
