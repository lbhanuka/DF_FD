package com.epic.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class CommonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonServiceApplication.class, args);
	}

	@LoadBalanced
	@Bean("internalCalls")
	RestTemplate restTemplateInternal() {
		return new RestTemplate();
	}

	@Bean("externalCalls")
	RestTemplate restTemplateExternal() {
		return new RestTemplate();
	}
}
