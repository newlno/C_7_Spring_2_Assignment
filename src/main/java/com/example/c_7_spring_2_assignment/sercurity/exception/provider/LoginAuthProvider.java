package com.example.c_7_spring_2_assignment.sercurity.exception.provider;

import com.example.c_7_spring_2_assignment.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/*
    Provider(인증처리 담당)
    Spring Security는 Filter가 요청한 인증 처리를 할 수 있는 Provider를 찾고 실제 인증처리 절차를 진행

    -> Client에서 전달한 ID/PW가 DB의 ID/PW와 일치하는지 인증
 */
@RequiredArgsConstructor
public class LoginAuthProvider implements AuthenticationProvider {

    @Resource(name = "userDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //LoginFilter에서 생성된 Token으로부터 ID/PW 조회
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) authentication;

        String username = authenticationToken.getName();
        String password = (String) authenticationToken.getCredentials();

        //UserDetailsService를 통해 DB에서 username으로 사용자 조회
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        // 처음 패스워드를 통한 로그인시
        if(password != null) {
            if(!passwordEncoder.matches(password,userDetails.getPassword())) {
                throw new BadCredentialsException(userDetails.getUsername() + " Invalid password");
            }
        }
        //인증성공시 -> LoginSuccessHandler 가 호출!
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }

    /* 인증처리 가능 여부 판단기준: "인증정보의 클래스 타입"을 보고 판단
        -> 인증을 요청한 LoginFilter는 "UsernamePasswordAuthenticationToken" 인증정보를 생성하여 요청하였음
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
