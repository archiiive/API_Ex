package com.example.api01.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    //http://localhost:8080/swagger-ui/index.html#/sample-json-controller/helloArr
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info().title("SpringDoc SwaggerUI Example")
                        .description("Test SwaggerUI application")
                        .version("v0.0.1"));
    }
}
