#Stage 1: Build
#Start with Maven  image that include JDK

FROM maven:3.9.11-amazoncorretto-21-debian-bookworm AS BUILD

#Copy source code and pom.xml file to /app folder
WORKDIR /app
COPY pom.xml .
COPY src ./src

#Build source code with manven

RUN mvn package -DskipTests

#Stage 2: create image
#Start with Amazon Correto JDK 21
FROM amazoncorretto:21.0.8

#Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

#Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

