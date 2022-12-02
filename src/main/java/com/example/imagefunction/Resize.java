package com.example.imagefunction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.function.Function;

import javax.imageio.ImageIO;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.PublicAccessType;
import com.azure.storage.blob.options.BlobContainerCreateOptions;
import com.azure.storage.blob.specialized.BlobOutputStream;

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

	private final BlobContainerClient blobContainerClient;

	public Resize(BlobServiceClient blobServiceClient, RestTemplateBuilder restTemplateBuilder) {
		BlobContainerCreateOptions options = new BlobContainerCreateOptions();
		options.setPublicAccessType(PublicAccessType.BLOB);
		this.blobContainerClient = blobServiceClient.createBlobContainerIfNotExistsWithResponse("images", options, Context.NONE).getValue();
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

		BlobClient blobClient = this.blobContainerClient.getBlobClient(filename);
		BlobHttpHeaders blobHttpHeaders = new BlobHttpHeaders();
		blobHttpHeaders.setContentType("image/" + format);
		try (BlobOutputStream blobOutputStream = blobClient.getBlockBlobClient().getBlobOutputStream(true)) {
			if (options.ratio() > 0) {
				resize(ImageIO.read(inputStream), blobOutputStream, format, options.ratio());
			}
			else {
				Assert.isTrue(options.width() > 0, "width parameter should be > 0 when percent parameter is not specified");
				Assert.isTrue(options.height() > 0, "height parameter should be > 0 when percent parameter is not specified");
				resize(ImageIO.read(inputStream), blobOutputStream, format, options.width(), options.height());
			}
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
		blobClient.setHttpHeaders(blobHttpHeaders);
		return blobClient.getBlobUrl();
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
