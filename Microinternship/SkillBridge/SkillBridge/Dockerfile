# -------------------------
# Stage 1: Build the JAR
# -------------------------
FROM maven:3.9.0-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first (to leverage Docker caching)
COPY pom.xml .

# Download dependencies only
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the Spring Boot JAR (skip tests for faster build)
RUN mvn clean package -DskipTests

# -------------------------
# Stage 2: Run the application
# -------------------------
FROM openjdk:17-jre-slim

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/SkillBridge-0.0.1-SNAPSHOT.jar app.jar

# Optional: expose port (just for documentation)
EXPOSE 8080

# Entry point
ENTRYPOINT ["java", "-jar", "app.jar"]
