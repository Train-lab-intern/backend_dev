## Image to build the project
#FROM maven:3.8.4-openjdk-17 AS builder
#
## Copy source code
#COPY api /backend_dev/api
#COPY common /backend_dev/common
#COPY pom.xml /backend_dev/pom.xml
#
## Building the Maven project
#WORKDIR /backend_dev
#RUN mvn clean package -DskipTests
#
## Image for api module
#FROM openjdk:17 AS api
#COPY --from=builder /backend_dev/api/target/*.jar /backend_dev/api.jar
##WORKDIR /backend_dev
##EXPOSE 8080
##ENTRYPOINT ["java", "-jar", "api.jar"]
#
## Image for common module
#FROM openjdk:17 AS common
#COPY --from=builder /backend_dev/common/target/*.jar /backend_dev/common.jar
#
## Image for combining modules
#FROM openjdk:17
#COPY --from=api /backend_dev /backend_dev
#COPY --from=common /backend_dev /backend_dev
#WORKDIR /backend_dev
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "api.jar"]

# Image to build the project
FROM maven:3.8.4-openjdk-17 AS builder

# Copy source code
COPY api /backend_dev/api
COPY common /backend_dev/common
COPY pom.xml /backend_dev/pom.xml

# Building the Maven project
WORKDIR /backend_dev
RUN mvn clean package -DskipTests

# Установка Flyway
RUN curl -L -o flyway-commandline-7.10.0-linux-x64.tar.gz https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/7.10.0/flyway-commandline-7.10.0-linux-x64.tar.gz \
    && tar xvzf flyway-commandline-7.10.0-linux-x64.tar.gz \
    && mv flyway-7.10.0 /flyway \
    && rm flyway-commandline-7.10.0-linux-x64.tar.gz

# Установка переменной среды для указания расположения миграционных скриптов
ENV FLYWAY_LOCATIONS=/common/src/main/resources/db/migration

# Image for api module
FROM openjdk:17 AS api
COPY --from=builder /backend_dev/api/target/*.jar /backend_dev/api.jar
COPY --from=builder /flyway /flyway
COPY --from=builder /backend_dev/common /backend_dev/common

WORKDIR /backend_dev
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api.jar"]