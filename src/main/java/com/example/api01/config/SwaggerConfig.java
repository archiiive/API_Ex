package com.example.api01.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    //http://localhost:8080/swagger-ui/index.html#/sample-json-controller/helloArr
    @Bean
    public OpenAPI openAPI(){

        //Security스키마 설정
        //model사용
        SecurityScheme baererAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        //Security 요청설정
        SecurityRequirement addSecutiry = new SecurityRequirement();
        addSecutiry.addList("Authorization");



        return new OpenAPI()
                .components(
                        new Components().addSecuritySchemes("Authorization",baererAuth)
                )
                //API마다 security 인증 컴포넌트 설정
                .info(new Info().title("SpringDoc SwaggerUI Example")
                        .description("Test SwaggerUI application")
                        .version("v0.0.1"));
    }
}
