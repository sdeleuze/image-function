package com.example.imagefunction;

import java.nio.ByteBuffer;

import com.azure.spring.cloud.autoconfigure.implementation.storage.blob.properties.AzureStorageBlobProperties;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.azure.storage.blob.models.PublicAccessType;
import com.azure.storage.blob.options.BlobContainerCreateOptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
@RegisterReflectionForBinding({ VisionRequestBody.class, VisionResponseBody.class })
class ImageController {

	private final BlobContainerAsyncClient blobContainerClient;

	private final WebClient webClient;

	private final VisionProperties visionProperties;

	ImageController(BlobServiceAsyncClient blobServiceClient, AzureStorageBlobProperties blobProperties,
			WebClient.Builder webClientBuilder, VisionProperties visionProperties) {
		BlobContainerCreateOptions options = new BlobContainerCreateOptions();
		options.setPublicAccessType(PublicAccessType.BLOB);
		this.blobContainerClient = blobServiceClient.getBlobContainerAsyncClient(blobProperties.getContainerName());
		this.blobContainerClient.createIfNotExistsWithResponse(options).block();
		this.visionProperties = visionProperties;
		this.webClient = webClientBuilder.baseUrl(visionProperties.url())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader("Ocp-Apim-Subscription-Key", visionProperties.key()).build();
	}

	@PostMapping("/process")
	Mono<ImageProcessingResult> process(@RequestBody Flux<ByteBuffer> requestBody, @RequestParam String filename) {
		BlobAsyncClient blobClient = this.blobContainerClient.getBlobAsyncClient(filename);
		BlobHttpHeaders blobHttpHeaders = new BlobHttpHeaders();
		blobHttpHeaders.setContentType("image/" + getFormat(filename));
		String blobUrl = blobClient.getBlobUrl();
		return blobClient
				.upload(requestBody, new ParallelTransferOptions(), true)
				.flatMap(v -> blobClient.setHttpHeaders(blobHttpHeaders)
						.then(this.webClient.post().bodyValue(new VisionRequestBody(blobUrl)).retrieve().bodyToMono(VisionResponseBody.class))
						.flatMap(visionResponseBody -> validate(visionResponseBody, blobClient))
						.map(visionResponseBody -> new ImageProcessingResult(blobUrl, visionResponseBody.tags())));

	}

	@ExceptionHandler(InvalidImageException.class)
	ResponseEntity<String> invalidImage(InvalidImageException ex) {
		return unprocessableEntity().body(ex.getMessage());
	}

	private Mono<VisionResponseBody> validate(VisionResponseBody visionResponseBody, BlobAsyncClient blobClient) {
		if (visionProperties.mandatoryTag() != null && visionResponseBody.tags().stream().noneMatch(tag ->
			tag.name().equals(visionProperties.mandatoryTag()) && tag.confidence() > 0.5
		)) {
			return blobClient.deleteIfExists().then(Mono.error(new InvalidImageException("Validation failed, tags need to contain " + visionProperties.mandatoryTag() + " with a confidence greater than 0.5")));
		} else {
			return Mono.just(visionResponseBody);
		}
	}

	private String getFormat(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

}
