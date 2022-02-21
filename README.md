# Simple API to manage Todo items

## Run with gradle

```shell
./gradlew bootRun
```

## Run with docker

### Build image

```shell
./gradlew clean docker
```

### Run container

```shell
./gradlew dockerRun
```

or

```shell
docker run --rm -p 8080:8080 --name todo-api quay.io/johschmidtcc/todo-api-spring-kotlin
```