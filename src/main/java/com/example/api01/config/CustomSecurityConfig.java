package com.example.api01.config;

import com.example.api01.security.APIUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {



    //p.785
    //주입 - 실제 인증처리를 위한 AuthenticationManager객체 설정이 필요하다.
    private final APIUserDetailService apiUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("----------------------W E B      |     C O N F I G U R E----------------------");

        //정적 소스 필터링 제외
        return (web -> web.ignoring()
                .requestMatchers(

                        PathRequest.toStaticResources().atCommonLocations()


                ));

    }



    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http )throws Exception{
        log.info("-------------------------- C O N F I G U R E --------------------------");
        //AuthenticationManager설정
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(apiUserDetailService)
                        .passwordEncoder(passwordEncoder());

        //P.787 GetAuthenticationManager
        //매니저를 설정하기위해서 빌더에
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //인증 매니저 등록
        http.authenticationManager(authenticationManager);



        //1.CSRF 토큰의 비활성화 활성화하면 나중에 토큰작업까지 다 처리를 해줘야한다
         http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
         //2. 세션을 사용하지 않는다.
         http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                 httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                         SessionCreationPolicy.STATELESS
                 ));
        return http.build();
    }




}