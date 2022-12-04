package com.example.imagefunction;

import java.util.Objects;

import org.springframework.lang.NonNull;

// TODO Migrate to records when supported by Azure Functions
public final class ResizeOptions {

	@NonNull
	private final String url;

	private final double ratio;

	private final int width;

	private final int height;

	public ResizeOptions(
			/** URL of the image to resize **/
			@NonNull String url,
			/** Ratio of the resized image compared to the source one, should be 0 when {@code width} and {@code height} are specified **/
			double ratio,
			/** Width of the resized image **/
			int width,
			/** Height of the resized image **/
			int height
	) {
		this.url = url;
		this.ratio = ratio;
		this.width = width;
		this.height = height;
	}

	@NonNull
	public String getUrl() {
		return url;
	}

	public double getRatio() {
		return ratio;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (ResizeOptions) obj;
		return Objects.equals(this.url, that.url) &&
				Double.doubleToLongBits(this.ratio) == Double.doubleToLongBits(that.ratio) &&
				this.width == that.width &&
				this.height == that.height;
	}

	@Override
	public int hashCode() {
		return Objects.hash(url, ratio, width, height);
	}

	@Override
	public String toString() {
		return "ResizeOptions[" +
				"url=" + url + ", " +
				"ratio=" + ratio + ", " +
				"width=" + width + ", " +
				"height=" + height + ']';
	}
}
