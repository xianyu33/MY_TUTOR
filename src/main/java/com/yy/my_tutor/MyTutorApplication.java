package com.yy.my_tutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class MyTutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyTutorApplication.class, args);
    }

}
