## Configure following configuration properties

spring.cloud.azure.storage.blob.account-name=...
spring.cloud.azure.storage.blob.account-key=...
spring.cloud.azure.storage.blob.endpoint=...

## Build for native

```
mvn -Pnative clean native:compile
```

## Sample HTTP request

```
http --raw='https://lirp.cdn-website.com/65289c0bc0e34ae191b0f3fd551b2996/dms3rep/multi/opt/GettyImages-1185878014-1920w.jpg' http://localhost:8080/resize
```