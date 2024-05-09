package com.example.api01.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

//REST콘트롤러다.
@RestController
@RequestMapping("/api/sample")
public class SampleController {


    private static final Logger log = LoggerFactory.getLogger(SampleController.class);

    @Operation(summary = "Sample Get DoA")
    @GetMapping("/doA")

    public List<String> doA(){
        log.info("-----------------do A 접근 ----------------");
        return Arrays.asList("AAA","BBB","CCC");

    }
}
