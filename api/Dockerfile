FROM bellsoft/liberica-openjdk-alpine:17 AS build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY uploads uploads
RUN ./mvnw install -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17

ENV SPRING_DATASOURCE_URL=
ENV SPRING_DATASOURCE_USERNAME=
ENV SPRING_DATASOURCE_PASSWORD=
ENV SECRET=
ENV SENDGRID_API_KEY=

WORKDIR /app
COPY --from=build /workspace/app/target/*.jar /app/app.jar
COPY --from=build /workspace/app/uploads /app/uploads
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
