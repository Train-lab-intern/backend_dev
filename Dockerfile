FROM alpine:3.20.3 AS base

RUN apk add --no-cache wget bash maven openjdk17

FROM base AS build

WORKDIR /app/it-roast
COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-alpine AS runtime
WORKDIR /app/it-roast
COPY --from=build /app/it-roast/target/*.jar it-roast.jar
ENTRYPOINT ["java", "-jar", "it-roast.jar"]
EXPOSE 8080

# Copy source code
#COPY common /backend_dev/common

# Building the Maven project
#RUN mvn clean package -DskipTests

# Image for common module
#FROM openjdk:17 AS common
#COPY --from=builder /backend_dev/common/target/*.jar /backend_dev/common.jar

# Image for combining modules
#FROM openjdk:17
#COPY --from=common /backend_dev /backend_dev
#WORKDIR /backend_dev
#EXPOSE 8080
#ENTRYPOINT ["common.jar"]
#CMD ["java", "-jar"]
