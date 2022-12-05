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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ImageController {

	private final BlobContainerAsyncClient blobContainerClient;

	ImageController(BlobServiceAsyncClient blobServiceClient, AzureStorageBlobProperties blobProperties) {
		BlobContainerCreateOptions options = new BlobContainerCreateOptions();
		options.setPublicAccessType(PublicAccessType.BLOB);
		this.blobContainerClient = blobServiceClient.getBlobContainerAsyncClient(blobProperties.getContainerName());
		this.blobContainerClient.createIfNotExistsWithResponse(options).block();
	}

	@PostMapping("/process")
	Mono<ImageProcessingResult> process(@RequestBody Flux<ByteBuffer> requestBody, @RequestParam String filename) {
		BlobAsyncClient blobClient = this.blobContainerClient.getBlobAsyncClient(filename);
		BlobHttpHeaders blobHttpHeaders = new BlobHttpHeaders();
		blobHttpHeaders.setContentType("image/" + getFormat(filename));
		return blobClient.upload(requestBody, new ParallelTransferOptions(), true)
				.flatMap(item -> blobClient.setHttpHeaders(blobHttpHeaders))
				.thenReturn(new ImageProcessingResult(blobClient.getBlobUrl()));
	}

	private String getFormat(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

}
