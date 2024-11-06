FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE=target/votacao-*.jar
COPY ${JAR_FILE} votacao.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "votacao.jar","--spring.profiles.active=prd"]