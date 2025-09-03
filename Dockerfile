# Use a small JDK image
FROM eclipse-temurin:17-jdk-alpine

# Set workdir
WORKDIR /app

# Copy pom and source
COPY . .

# Build the app (requires Maven wrapper or Maven installed in container)
RUN ./mvnw clean package -DskipTests

# Expose Render's port
EXPOSE 8080

# Run the app (replace with your actual jar name if different)
CMD ["java", "-jar", "target/todo-0.0.1-SNAPSHOT.jar"]
