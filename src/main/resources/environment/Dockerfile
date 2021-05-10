#Dockerfile with basic Java 
FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/mutant-svc-1.0.0.jar
ADD ${JAR_FILE} mutant-svc-1.0.0.jar
ENTRYPOINT ["java","-jar","/mutant-svc-1.0.0.jar"]