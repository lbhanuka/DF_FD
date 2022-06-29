package com.epic.fdservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FdServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FdServiceApplication.class, args);
	}

}
