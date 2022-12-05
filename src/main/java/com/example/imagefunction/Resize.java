package com.example.imagefunction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

/**
 * Function that takes as input the URL of the image
 */
@Component
public class Resize implements Function<ResizeOptions, String> {

	private final RestTemplate restTemplate;

	public Resize(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(5))
				.setReadTimeout(Duration.ofSeconds(5)).build();
	}

	@Override
	public String apply(ResizeOptions options) {
		byte[] imageData = restTemplate.getForObject(options.url(), byte[].class);
		Assert.notNull(imageData, "imageData should not be null");
		InputStream inputStream = new ByteArrayInputStream(imageData);

		String filename = getFilename(options.url());
		String format = getFormat(filename);

		Assert.isTrue(options.ratio() > 0 || options.width() > 0 || options.height() > 0, "Please specify at least ratio, width or height option");
		try {
			String tmpdir = Files.createTempDirectory("tmpDirPrefix").toFile().getAbsolutePath();
			File resizedImageFile = new File(tmpdir + File.separator + filename);
			if (options.ratio() > 0) {
				resize(ImageIO.read(inputStream), new FileOutputStream(resizedImageFile), format, options.ratio());
			}
			else {
				resize(ImageIO.read(inputStream), new FileOutputStream(resizedImageFile), format, options.width(), options.height());
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		return "";
	}

	private String getFilename(String url) {
		try {
			return Paths.get(new URI(url).getPath()).getFileName().toString();
		}
		catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	private String getFormat(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	private void resize(BufferedImage inputImage,
			OutputStream outputStream, String format, int width, int height)
			throws IOException {

		if (width == 0) {
			width = (int) (inputImage.getWidth() * (height / (float) inputImage.getHeight()));
		}
		if (height == 0) {
			height = (int) (inputImage.getHeight() * (width / (float) inputImage.getWidth()));
		}
		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
		Graphics2D graphics2D = outputImage.createGraphics();
		graphics2D.drawImage(inputImage, 0, 0, width, height, null);
		graphics2D.dispose();
		ImageIO.write(outputImage, format, outputStream);
	}

	private void resize(BufferedImage inputImage,
			OutputStream outputStream, String format, double ratio) throws IOException {
		int scaledWidth = (int) (inputImage.getWidth() * ratio);
		int scaledHeight = (int) (inputImage.getHeight() * ratio);
		resize(inputImage, outputStream, format, scaledWidth, scaledHeight);
	}

}
