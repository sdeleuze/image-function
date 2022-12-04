## Pre-requisites

Checkout https://github.com/Azure/azure-sdk-for-java/pull/32417 and install it by following https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring#build-from-source instructions.

## Configure following configuration properties

```
spring.cloud.azure.storage.blob.account-name=...
spring.cloud.azure.storage.blob.account-key=...
spring.cloud.azure.storage.blob.endpoint=...
```

## Build & run

To run with SCF exposed via Spring MVC
```
./mvnw -Pweb spring-boot:run
```

## Sample HTTP request

Supported on both JVM and native:
```
http --json http://localhost:8080/resize url='https://offloadmedia.feverup.com/parissecret.com/wp-content/uploads/2020/12/23045637/1-1-1024x576.png' ratio=0.8
http --json http://localhost:8080/resize url='https://offloadmedia.feverup.com/parissecret.com/wp-content/uploads/2020/12/23045637/1-1-1024x576.png' width=200 height=100
http --json http://localhost:8080/resize url='https://offloadmedia.feverup.com/parissecret.com/wp-content/uploads/2020/12/23045637/1-1-1024x576.png' width=200
http --json http://localhost:8080/resize url='https://offloadmedia.feverup.com/parissecret.com/wp-content/uploads/2020/12/23045637/1-1-1024x576.png' height=100
```

Only supported on JVM:
```
http --json http://localhost:8080/resize url='https://lirp.cdn-website.com/65289c0bc0e34ae191b0f3fd551b2996/dms3rep/multi/opt/GettyImages-1185878014-1920w.jpg' ratio=0.8
http --json http://localhost:8080/resize url='https://lirp.cdn-website.com/65289c0bc0e34ae191b0f3fd551b2996/dms3rep/multi/opt/GettyImages-1185878014-1920w.jpg' width=200 height=100
http --json http://localhost:8080/resize url='https://lirp.cdn-website.com/65289c0bc0e34ae191b0f3fd551b2996/dms3rep/multi/opt/GettyImages-1185878014-1920w.jpg' width=200
http --json http://localhost:8080/resize url='https://lirp.cdn-website.com/65289c0bc0e34ae191b0f3fd551b2996/dms3rep/multi/opt/GettyImages-1185878014-1920w.jpg' height=100
```