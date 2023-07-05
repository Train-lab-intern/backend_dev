# Сборка проекта с помощью Maven
FROM maven:4.0.0-openjdk-17 AS builder

# Копирование исходного кода
COPY api /backend_dev/api/pom.xml
COPY common /backend_dev/common/pom.xml
COPY pom.xml /backend_dev/pom.xml

# Сборка проекта Maven
WORKDIR /backend_dev
RUN mvn clean package -DskipTests

# Образ для модуля api
FROM openjdk:17 AS api
COPY api/target/*.jar /backend_dev/api.jar
WORKDIR /backend_dev
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api.jar"]

# Образ для модуля common
FROM openjdk:17 AS common
COPY common/target/*.jar /backend_dev/common.jar

# Образ для объединения модулей
FROM openjdk:17
COPY --from=api /backend_dev /backend_dev
COPY --from=common /backend_dev /backend_dev
WORKDIR /backend_dev
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api.jar"]
