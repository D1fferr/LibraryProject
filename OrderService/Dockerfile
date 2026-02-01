FROM openjdk:22-jdk-slim AS build

WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

FROM openjdk:22-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]