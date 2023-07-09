# Image to build the project
FROM maven:3.8.4-openjdk-17 AS builder

# Copy source code
COPY api /backend_dev/api
COPY common /backend_dev/common
COPY pom.xml /backend_dev/pom.xml

# Building the Maven project
WORKDIR /backend_dev
RUN mvn clean package -DskipTests

# Image for api module
FROM openjdk:17 AS api
COPY --from=builder /backend_dev/api/target/*.jar /backend_dev/api.jar
WORKDIR /backend_dev
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api.jar"]

# Image for common module
FROM openjdk:17 AS common
COPY --from=builder /backend_dev/common/target/*.jar /backend_dev/common.jar

# Image for combining modules
FROM openjdk:17
COPY --from=api /backend_dev /backend_dev
COPY --from=common /backend_dev /backend_dev
WORKDIR /backend_dev
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api.jar"]