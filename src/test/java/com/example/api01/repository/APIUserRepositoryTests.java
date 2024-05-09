package com.example.api01.repository;

import com.example.api01.domain.APIUser;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2

public class APIUserRepositoryTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private APIUserRepository apiUserRepository;

    @Test
    public void testInsert(){
        log.info("-----------------------------T E S T   |   I N S E R T-----------------------------");

        IntStream.range(1,100).forEach(i -> {
            APIUser apiUser = APIUser.builder()
                    .mid("apiuser" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .build();
            apiUserRepository.save(apiUser);
            log.info("-----------------------------T E S T   |   I N S E R T  end -----------------------------");
        });
    }
}
