Image service that leverages Azure Storage and Azure Computer Vision via a Spring Boot 3 application that can be compiled to native.

## Configure following configuration properties

```
spring.cloud.azure.storage.blob.account-name=...
spring.cloud.azure.storage.blob.account-key=...
spring.cloud.azure.storage.blob.endpoint=...
vision.url=...
vision.key=...
```

## Build & run

To build as a native executable
```
./mvnw -Pnative clean native:compile
target/image-service
```

To build a native container image for deployment in the Cloud
```
./mvn -Pnative spring-boot:build-image
```