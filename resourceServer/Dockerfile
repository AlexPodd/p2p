FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
COPY target/resource-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]
