FROM openjdk:17-jdk-slim
RUN mkdir app
COPY . /app
WORKDIR /app
RUN chmod+x mvnw
RUN ./mvnw clean
RUN ./mvnw package
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
