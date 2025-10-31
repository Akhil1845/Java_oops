# Stage 1: Build the JAR
FROM maven:3.9.0-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build JAR (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/SkillBridge-0.0.1-SNAPSHOT.jar app.jar

# Expose port (documentation only)
EXPOSE 8080

# Start Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
