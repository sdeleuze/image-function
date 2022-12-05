package com.example.imagefunction;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class ImageFunctionApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ImageFunctionApplication.class);
		String functionServerPort = System.getenv("FUNCTIONS_CUSTOMHANDLER_PORT");
		if (functionServerPort != null) {
			app.setDefaultProperties(Collections
					.singletonMap("server.port", functionServerPort));
		}
		app.run(args);
	}

}
