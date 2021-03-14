#FROM maven:3.6.3-openjdk-8 AS build
#WORKDIR /build
#COPY pom.xml .
#RUN mvn dependency:go-offline -B
#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#RUN mvn -f /usr/src/app/pom.xml clean install -Dmaven.test.skip=true

FROM amazoncorretto:latest
#COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
COPY /target/*.jar /usr/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/app.jar"]