package com.actuator.actuator1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Actuator1Application {

	public static void main(String[] args) {
		SpringApplication.run(Actuator1Application.class, args);
	}

}
