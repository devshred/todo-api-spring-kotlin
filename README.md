# Simple API to manage Todo items

## Run with gradle
```./gradlew bootRun```
```./gradlew bootRun --args='--server.port=7001'```

## Run with docker
```./gradlew clean build docker```
```./gradlew dockerRun ```
```docker run --rm -p 7001:8080 --name todo-api todo-api```