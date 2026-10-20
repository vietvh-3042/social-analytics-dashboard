FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew

COPY src ./src
RUN ./gradlew --no-daemon bootJar

FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app
RUN addgroup -S app && adduser -S -G app app
COPY --from=build --chown=app:app /workspace/build/libs/*.jar /app/app.jar

USER app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
