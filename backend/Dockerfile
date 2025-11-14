### build stage ###
FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -ntp clean package -DskipTests

### runtime stage ###
FROM eclipse-temurin:11-jre AS runtime

RUN apt-get update \
    && apt-get install -y --no-install-recommends ffmpeg \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /workspace/target/annotation-platform-backend-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod \
    APP_STORAGE_ROOT=/data/annotation \
    APP_MEDIA_PREVIEW_FFMPEG=/usr/bin/ffmpeg

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]

