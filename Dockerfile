FROM node:20-alpine AS frontend
WORKDIR /app
COPY nutriflow-frontend/package*.json ./
RUN npm ci
COPY nutriflow-frontend/ .
RUN npm run build

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY nutriflow-backend/pom.xml .
RUN mvn dependency:go-offline -q
COPY nutriflow-backend/src ./src
COPY --from=frontend /app/dist ./src/main/resources/static
RUN mvn package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
