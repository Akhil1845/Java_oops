# Use official OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy the built JAR from your project
COPY target/SkillBridge-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will set PORT env variable)
EXPOSE 8080

# Set environment variable for Render port
ENV PORT 8080

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
