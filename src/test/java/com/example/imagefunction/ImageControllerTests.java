package com.example.imagefunction;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageControllerTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void upload() {
		String filename = "marcel.jpg";
		ClassPathResource marcel = new ClassPathResource(filename);
		webTestClient.post().uri("process?filename=" + filename)
				.body(BodyInserters.fromResource(marcel)).exchange().expectStatus().is2xxSuccessful()
				.expectBody(ImageProcessingResult.class).consumeWith(entityResult -> {
					ImageProcessingResult result = entityResult.getResponseBody();
					Assertions.assertThat(result.url()).startsWith("https://");
				});
	}

}
