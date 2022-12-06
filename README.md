## Pre-requisites

Checkout https://github.com/Azure/azure-sdk-for-java/tree/feature/spring-boot-3 and install it by following https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring#build-from-source instructions.

```
git clone https://github.com/Azure/azure-sdk-for-java.git
cd azure-sdk-for-java
git checkout feature/spring-boot-3
mvn clean install \
  -Dcheckstyle.skip=true \
  -Dcodesnippet.skip \
  -Denforcer.skip \
  -Djacoco.skip=true \
  -Dmaven.javadoc.skip=true \
  -Drevapi.skip=true \
  -DskipTests \
  -Dspotbugs.skip=true \
  -Pdev \
  -T 4 \
  -ntp \
  -f sdk/spring/pom.xml
```

## Configure following configuration properties

```
spring.cloud.azure.storage.blob.account-name=...
spring.cloud.azure.storage.blob.account-key=...
spring.cloud.azure.storage.blob.endpoint=...
```

## Build

To build as a native image
```
./mvnw -Pnative clean native:compile
target/image-function
```