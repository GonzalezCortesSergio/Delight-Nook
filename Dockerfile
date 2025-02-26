FROM bellsoft/liberica-openjdk-alpine:17 AS build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw install -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://delight-nook-db/Delight-Nook
ENV SPRING_DATASOURCE_USERNAME=admin
ENV SPRING_DATASOURCE_PASSWORD=1234
ENV SECRET=
ENV SENDGRID_API_KEY=

WORKDIR /app
COPY --from=build /workspace/app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
