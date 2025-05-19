# Use a minimal base image with Java
FROM openjdk:17-jdk-slim

# Add metadata
LABEL maintainer="yayanagustyan@gmail.com"

# Set app directory
WORKDIR /app

# Copy the jar file into the container
COPY target/*.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 1212

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
