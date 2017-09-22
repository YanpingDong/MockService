package com.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableZipkinServer
public class MockServiceApplication {
	
	/*
	 * fake service start point
	 * @param args input params
	 */
	public static void main(final String[] args) {
		SpringApplication.run(MockServiceApplication.class, args);
	}
}
