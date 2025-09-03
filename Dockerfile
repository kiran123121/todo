# Use official Java 17 runtime as base
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR built by Maven (correct filename here!)
COPY target/TodoApplication-1-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render will provide PORT dynamically)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
