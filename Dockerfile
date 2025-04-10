FROM maven:3.8.7-openjdk-18 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1
COPY --from=build target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]