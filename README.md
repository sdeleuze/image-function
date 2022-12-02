## Pre-requisites

Clone and install https://github.com/Azure/azure-sdk-for-java/tree/feature/spring-boot-3 following https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring#build-from-source instructions.

## Configure following configuration properties

```
spring.cloud.azure.storage.blob.account-name=...
spring.cloud.azure.storage.blob.account-key=...
spring.cloud.azure.storage.blob.endpoint=...
```

## Build for native

```
mvn -Pnative clean native:compile
```

Currently broken by the following error after startup:
```
Caused by: com.azure.storage.blob.models.BlobStorageException: Status code 400, "﻿<?xml version="1.0" encoding="utf-8"?><Error><Code>InvalidHeaderValue</Code><Message>The value for one of the HTTP headers is not in the correct format.
Time:2022-12-02T11:41:42.4023028Z</Message><HeaderName>x-ms-blob-public-access</HeaderName><HeaderValue>{}</HeaderValue></Error>"
        at java.base@17.0.5/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499) ~[image-function:na]
        ...
        Suppressed: java.lang.Exception: #block terminated with an error
                at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:99) ~[image-function:3.5.0]
                at reactor.core.publisher.Mono.block(Mono.java:1710) ~[image-function:3.5.0]
                at com.azure.storage.common.implementation.StorageImplUtils.blockWithOptionalTimeout(StorageImplUtils.java:156) ~[na:na]
                at com.azure.storage.blob.BlobContainerClient.createIfNotExistsWithResponse(BlobContainerClient.java:374) ~[na:na]
                at com.azure.storage.blob.BlobServiceClient.createBlobContainerIfNotExistsWithResponse(BlobServiceClient.java:210) ~[image-function:12.20.0]
                at com.example.imagefunction.Resize.<init>(Resize.java:44) ~[image-function:na]
                ...
                at com.example.imagefunction.ImageFunctionApplication.main(ImageFunctionApplication.java:10) ~[image-function:na]
```

## Sample HTTP request

```
http --json http://localhost:8080/resize url='https://offloadmedia.feverup.com/parissecret.com/wp-content/uploads/2020/12/23045637/1-1-1024x576.png' ratio=0.8
http --json http://localhost:8080/resize url='https://offloadmedia.feverup.com/parissecret.com/wp-content/uploads/2020/12/23045637/1-1-1024x576.png' width=200 height=100
http --json http://localhost:8080/resize url='https://lirp.cdn-website.com/65289c0bc0e34ae191b0f3fd551b2996/dms3rep/multi/opt/GettyImages-1185878014-1920w.jpg' ratio=0.8
http --json http://localhost:8080/resize url='https://lirp.cdn-website.com/65289c0bc0e34ae191b0f3fd551b2996/dms3rep/multi/opt/GettyImages-1185878014-1920w.jpg' width=200 height=100
```