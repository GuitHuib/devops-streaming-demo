FROM selenium/standalone-firefox:latest

USER root
# Install necessary dependencies.
RUN apt-get update -y \
    && apt-get install -y openjdk-17-jdk \
    && apt-get clean
ENV MOZ_HEADLESS=1
USER seluser

WORKDIR /app
COPY build/libs/*.jar /app/scrape-demo.jar
COPY /src/main/resources/application.properties /app/application.properties
CMD ["java", "-jar", "scrape-demo.jar"]


## Use an official OpenJDK runtime as a parent image
#FROM openjdk:17-oracle
#
## Set the working directory in the container
#WORKDIR /app
#
## Copy the Gradle build files and dependencies to the container
#COPY build/libs/*.jar /app/scrape-demo.jar
#
#COPY src/main/resources/application.properties /app/application.properties
#
## Expose a port (if your Spring Boot app uses a specific port)
##EXPOSE 8080
#
## Define the command to run your Spring Boot application
#CMD ["java", "-jar", "scrape-demo.jar"]