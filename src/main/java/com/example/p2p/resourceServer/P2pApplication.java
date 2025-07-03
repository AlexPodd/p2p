package com.example.p2p.resourceServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.p2p.resourceServer")
public class P2pApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-resource");
		SpringApplication.run(P2pApplication.class, args);
	}

}
