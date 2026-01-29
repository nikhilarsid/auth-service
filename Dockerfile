# Stage 1: Build the application
FROM gradle:jdk17-alpine AS build
WORKDIR /app
COPY . .
# Build the JAR, skipping tests to speed up deployment
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Run the application
# Using eclipse-temurin (Standard for Java 17)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the Auth Service port
EXPOSE 8060

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]