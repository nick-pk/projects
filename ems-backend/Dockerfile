# Use official OpenJDK base image for Java
# Compilation
FROM openjdk:17-jdk-slim AS builder

# Set the working directory
WORKDIR /app

# Copy the backend project (including mvnw) into the container
COPY . /app

# Build the app (assuming you're using Maven here)
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Run
# Use a minimal base image for running the app
FROM openjdk:17-jdk-slim

# Install curl (required for health check or debugging)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
