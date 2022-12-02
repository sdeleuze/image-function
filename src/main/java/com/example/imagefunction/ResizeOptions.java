package com.example.imagefunction;

import org.springframework.lang.NonNull;

public record ResizeOptions(
		/** URL of the image to resize **/
		@NonNull String url,
		/** Ratio of the resized image compared to the source one, should be 0 when {@code width} and {@code height} are specified **/
		double ratio,
		/** Width of the resized image **/
		int width,
		/** Height of the resized image **/
		int height
) { }
