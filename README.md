# Todo-API based on Spring Boot and Kotlin
This project uses Spring Boot and Kotlin to provide a simple todo-app API and is used for sample deployments in the scope of an [OKD](https://www.okd.io) workshop.
It implements an [OpenAPI spec](https://raw.githubusercontent.com/devshred/todo-api-spring-kotlin/main/src/main/resources/todo-spec.yaml) and can be tested with a [frontend based on Vue.js](https://github.com/devshred/todo-web).

## Run with gradle
```shell
./gradlew bootRun
```

## Run with docker (local image)
### Build image

```shell
./gradlew clean docker
```

### Run container

```shell
./gradlew dockerRun
```

## Run with docker (image at Quay.io)
```shell
docker run --rm -p 8080:8080 --name todo-api quay.io/johschmidtcc/todo-api-spring-kotlin
```
