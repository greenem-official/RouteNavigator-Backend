FROM eclipse-temurin:21-jdk-alpine as makeImage

WORKDIR /app

# May need to be modified if any changes to file name happen
COPY build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
