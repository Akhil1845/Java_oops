# ----------------------------------------------------
# Stage 1: Build the JAR (using Maven to compile code)
# ----------------------------------------------------
# Use a Maven image with Java 17 for the build environment
FROM maven:3.9.0-eclipse-temurin-17 AS build

# Set the working directory for the build process
WORKDIR /app

# Copy pom.xml first to leverage Docker cache for dependencies
COPY pom.xml ./

# Download dependencies (if pom.xml hasn't changed, this step is cached)
RUN mvn dependency:go-offline

# Copy application source code
COPY src ./src

# Build the Spring Boot JAR (skip tests for faster build)
# The resulting JAR is named SkillBridge-0.0.1-SNAPSHOT.jar in the target folder
RUN mvn clean package -DskipTests

# ----------------------------------------------------
# Stage 2: Run the application (using only JRE for runtime)
# ----------------------------------------------------
# Use a lightweight JRE image (Java Runtime Environment) for the final container
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the final container
WORKDIR /app

# Copy the final JAR from the 'build' stage
# Ensure this JAR name matches the output of your Maven build
COPY --from=build /app/target/SkillBridge-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (documentation only)
EXPOSE 8080

# Environment variables for PostgreSQL (can be overridden by environment)
# These are optional since they are also in application.properties,
# but defining them here makes them environment-specific if needed.
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-d424dp63jp1c73ab0bug-a.oregon-postgres.render.com:5432/skillbridge_db_dd17
ENV SPRING_DATASOURCE_USERNAME=skillbridge_db_dd17_user
ENV SPRING_DATASOURCE_PASSWORD=GiG2ATdFk4rSA4ggxqLIk4l3e4lrk11d

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]