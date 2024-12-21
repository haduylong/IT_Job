# Stage 1: build the application
FROM gradle:8.7.0-jdk-lts-and-current-alpine AS build
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Stage 2: run the application
FROM openjdk:24-ea-21-slim
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar" , "app.jar"]