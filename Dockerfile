FROM openjdk:17-jdk-slim
RUN dos2unix ./mvnw
RUN ./mvnw clean
RUN ./mvnw package
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
