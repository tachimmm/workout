# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the contents of the local src directory to the container at /app
COPY . /app

# Build the application with Maven
RUN mvn clean package -Dmaven.test.skip=true
# Expose port 8080 to the outside world
EXPOSE 8080

# Define environment variable
ENV NAME World

# Run the application using spring-boot:run
CMD ["java", "-jar", "target/workout-0.0.1-SNAPSHOT.jar"]
