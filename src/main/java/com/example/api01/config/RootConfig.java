package com.example.api01.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //component 스프링으로들어간다.

public class RootConfig {

    @Bean //설정을 코드로 작업을 한다.
    public ModelMapper getMapper(){
        ModelMapper modelMapper = new ModelMapper();
        //모델매퍼의 설정을 가져와서 필드 매칭을 활성화 합니다.
        //소스 객체와 대상간의 필드 이름이 같으면 자동으로 매핑된다.
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }


}
