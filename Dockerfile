FROM gradle:8.12-jdk17 AS builder

COPY / /workspace
WORKDIR /workspace

RUN chmod +x gradlew && ./gradlew clean build

FROM eclipse-temurin:17

COPY --from=builder /workspace/build/libs/maru-0.0.1-SNAPSHOT.jar ./app.jar

ENV TZ=Asia/Seoul
EXPOSE 8080

VOLUME ["/logs"]

RUN mkdir -p /opt/datadog && wget -O /opt/datadog/dd-java-agent.jar 'https://dtdg.co/latest-java-tracer'

ENTRYPOINT ["java", "-javaagent:/opt/datadog/dd-java-agent.jar", "-Ddd.profiling.enabled=true", "-XX:FlightRecorderOptions=stackdepth=256", "-Ddd.logs.injection=true", "-Ddd.service=maru-backend", "-Ddd.env=prod", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
