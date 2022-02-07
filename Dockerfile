FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
ARG JAR_FILE=target/cake-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} cake-api-0.0.1-SNAPSHOT.jar
EXPOSE 8080 8282
ENTRYPOINT ["java","-jar","cake-api-0.0.1-SNAPSHOT.jar"]