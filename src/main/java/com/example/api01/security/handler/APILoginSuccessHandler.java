package com.example.api01.security.handler;

import com.example.api01.utill.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

//P.792
@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {



    private final JWTUtil jwtUtil;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {
        log.info("-----------------------------------LOGIN SUCCESS HANDLER-----------------------------------");

        //Athentication은 로그인
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authentication);
        log.info(authentication.getName()); //username

        //P.805 토큰 만들기
        // user,pw
        Map<String, Object> claim = Map.of("mid",authentication.getName());

        //Access Token 유효기간 1일
        String acessToken = jwtUtil.generateToken(claim,1);

        //Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claim,30);

        Gson gson = new Gson();
        Map<String,String> ketMap = Map.of(
                "accessToken",acessToken,
                "refreshToken",refreshToken
        );
        String jsonStr = gson.toJson(ketMap);
        response.getWriter().println(jsonStr);

        log.info("-----------------------------------LOGIN SUCCESS HANDLER END-----------------------------------");

    }


}
