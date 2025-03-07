#FROM eclipse-temurin:21-jdk-alpine AS build
#
#WORKDIR /app
#
#COPY . .
#
#RUN ./gradlew build --no-daemon -x test

FROM eclipse-temurin:21-jdk-alpine as makeImage

WORKDIR /app

#COPY --from=build /app/build/libs/*.jar app.jar
COPY build/libs/RouteNavigator-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
