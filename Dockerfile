# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-oracle

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle build files and dependencies to the container
COPY ${{ github.workspace }}/build/libs/*.jar /app/scrape-demo.jar

# Expose a port (if your Spring Boot app uses a specific port)
#EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "scrape-demo.jar"]