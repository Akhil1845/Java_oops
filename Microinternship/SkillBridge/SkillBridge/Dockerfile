# -------------------------------
# Stage 1: Build the JAR
# -------------------------------
FROM maven:3.9.0-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first to leverage Docker cache
COPY pom.xml ./

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build Spring Boot JAR (skip tests for faster build)
RUN mvn clean package -DskipTests

# -------------------------------
# Stage 2: Run the application
# -------------------------------
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/SkillBridge-0.0.1-SNAPSHOT.jar app.jar

# Expose port (documentation only)
EXPOSE 8080

# PostgreSQL environment variables (optional, can be overridden by Render)
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-d424dp63jp1c73ab0bug-a.oregon-postgres.render.com:5432/skillbridge_db_dd17
ENV SPRING_DATASOURCE_USERNAME=skillbridge_db_dd17_user
ENV SPRING_DATASOURCE_PASSWORD=GiG2ATdFk4rSA4ggxqLIk4l3e4lrk11d

# Start Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
