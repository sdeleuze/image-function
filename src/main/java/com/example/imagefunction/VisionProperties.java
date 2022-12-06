package com.example.imagefunction;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vision")
public record VisionProperties(String url, String key, String mandatoryTag) { }
