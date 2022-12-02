package com.example.imagefunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class ImageFunctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageFunctionApplication.class, args);
	}

}
