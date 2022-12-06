package com.example.imagefunction;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VisionResponseBody(String modelVersion, String requestId, List<Tag> tags) {}
