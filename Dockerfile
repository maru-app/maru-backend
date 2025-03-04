FROM gradle:8.12-jdk17 AS builder

COPY / /workspace
WORKDIR /workspace

RUN chmod +x gradlew && ./gradlew clean build

FROM eclipse-temurin:17

COPY --from=builder /workspace/build/libs/maru-0.0.1-SNAPSHOT.jar ./app.jar

ENV TZ Asia/Seoul
EXPOSE 8080

VOLUME ["/logs"]

ENTRYPOINT ["java", "-jar", "app.jar"]
