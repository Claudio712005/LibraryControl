FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/LibraryControl-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]