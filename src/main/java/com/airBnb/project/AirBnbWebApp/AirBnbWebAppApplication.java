package com.airBnb.project.AirBnbWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AirBnbWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirBnbWebAppApplication.class, args);
	}

}
