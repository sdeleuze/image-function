package com.example.imagefunction;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

@ConfigurationProperties(prefix = "vision")
public record VisionProperties(String url, String key, @Nullable String mandatoryTag) { }
