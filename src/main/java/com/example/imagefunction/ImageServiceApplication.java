package com.example.imagefunction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;

@SpringBootApplication(proxyBeanMethods = false)
@EnableConfigurationProperties(VisionProperties.class)
public class ImageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageServiceApplication.class, args);
	}

}

@ConfigurationProperties(prefix = "vision")
record VisionProperties(String url, String key, @Nullable String mandatoryTag) { }