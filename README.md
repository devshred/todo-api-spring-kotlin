# Simple API to manage Todo items

## Run with gradle

```shell
./gradlew bootRun --args='--server.port=7001'
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
docker run --rm -p 7001:8080 --name todo-api quay.io/johschmidtcc/todo-api-spring-kotlin
```