FROM openjdk:11-jre-slim
WORKDIR .
ARG JAR_FILE
COPY target/*.jar api.jar
EXPOSE 8080
CMD ["java", "-jar", "api.jar"]