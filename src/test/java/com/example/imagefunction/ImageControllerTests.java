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
	void uploadCat() {
		String filename = "marcel.jpg";
		ClassPathResource marcel = new ClassPathResource(filename);
		webTestClient.post().uri("process?filename=" + filename)
				.body(BodyInserters.fromResource(marcel)).exchange().expectStatus().is2xxSuccessful()
				.expectBody(ImageProcessingResult.class).consumeWith(entityResult -> {
					ImageProcessingResult result = entityResult.getResponseBody();
					Assertions.assertThat(result.url()).startsWith("https://");
					Assertions.assertThat(result.tags()).anySatisfy(tag -> {
						Assertions.assertThat(tag.name()).isEqualTo("cat");
						Assertions.assertThat(tag.confidence()).isGreaterThan(0.5f);
					});
				});
	}

	@Test
	void uploadRadiator() {
		String filename = "radiator.jpg";
		ClassPathResource radiator = new ClassPathResource(filename);
		webTestClient.post().uri("process?filename=" + filename)
				.body(BodyInserters.fromResource(radiator)).exchange().expectStatus().is5xxServerError();
	}

}
