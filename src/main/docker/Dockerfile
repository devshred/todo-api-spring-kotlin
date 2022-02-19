FROM eclipse-temurin:11.0.14_9-jre-alpine@sha256:2d9fe6caa720dc96202fca6f65e0d00d2db7a58bd4cc34251c52b9172b467b79

ARG JAR_FILE

RUN addgroup -S app-group && adduser -D -H -S app-user -G app-group -u 1000
USER app-user
EXPOSE 8080
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]