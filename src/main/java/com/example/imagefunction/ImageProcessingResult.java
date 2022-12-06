package com.example.imagefunction;

import java.util.List;

public record ImageProcessingResult(String url, List<Tag> tags) {}