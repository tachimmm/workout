# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY mvnw .
COPY .mvn .mvn


# Build the application with Maven
RUN ./mvnw clean package -DskipTests

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the application when the container launches
CMD ["java", "-jar", "target/usefulltools-0.0.1-SNAPSHOT.jar"]
