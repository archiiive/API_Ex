package com.example.api01.utill;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;


@SpringBootTest
@Log4j2
public class JWTUtillTests {

    @Autowired
    private JWTUtil jwtUtil;

    //P.798
    @Test
    public void TestGenerate(){
        Map<String,Object> claimMap =Map.of("mid","ABCDE");

        String jwtstr = jwtUtil.generateToken(claimMap,10);

        log.info(jwtstr);

    }
    @Test
    public void testValidate(){
            String jwtStr ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtaWQiOiJBQkNERSIsImlhdCI6MTcxNTMwNzA5NSwiZXhwIjoxNzE1MzA3Njk1fQ.03a9wKGdY6ejFd_eH5vrkFPl5AUJ-Ditx-gsyAEab5Y";

                Map<String,Object> claim = jwtUtil.validateToken(jwtStr);

        log.info(claim);
    }


}
