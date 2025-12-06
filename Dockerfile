FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew ./
COPY gradle gradle
COPY settings.gradle.kts ./
COPY build.gradle.kts ./
RUN chmod +x gradlew

RUN ./gradlew build -x test --no-daemon || true

COPY . .

RUN ./gradlew shadowJar --no-daemon -x test

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
