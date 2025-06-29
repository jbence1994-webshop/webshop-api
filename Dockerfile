FROM eclipse-temurin:21-alpine

WORKDIR /webshop

COPY target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/webshop/app.jar"]
