package com.example.backend_torneos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BackendTorneosApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTorneosApplication.class, args);
	}

}
