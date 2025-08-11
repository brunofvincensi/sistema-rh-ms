package com.rh.time_clock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TimeClockApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeClockApplication.class, args);
	}

}
