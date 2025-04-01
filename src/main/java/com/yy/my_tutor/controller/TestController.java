package com.yy.my_tutor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zingZone")
public class TestController {

    @PostMapping("/test")
    public String findPage() {
        return "s";
    }
}
