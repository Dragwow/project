FROM maven:4.0.0-rc-4-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn -pl user-service -am clean package -DskipTests

FROM eclipse-temurin:21-jdk

EXPOSE 8090
COPY --from=build /app/user-service/target/user-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]