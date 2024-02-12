package com.example.gesvet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.gesvet.controller")
public class GesvetApplication {

    public static void main(String[] args) {
        SpringApplication.run(GesvetApplication.class, args);
    }

}
