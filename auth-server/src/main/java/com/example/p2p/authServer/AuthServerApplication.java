package com.example.p2p.authServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.p2p.authServer")
public class AuthServerApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-auth");
        SpringApplication.run(AuthServerApplication.class, args);
    }

}

