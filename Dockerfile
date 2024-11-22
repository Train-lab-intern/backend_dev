#FROM alpine:3.20.3 AS base

#RUN apk add --no-cache wget bash maven openjdk17

#FROM base AS build

#WORKDIR /app/it-roast
#COPY . .

#RUN mvn clean package -DskipTests

#FROM openjdk:17-alpine AS runtime
#WORKDIR /app/it-roast
#COPY --from=build /app/it-roast/target/*.jar it-roast.jar
#ENTRYPOINT ["java", "-jar", "it-roast.jar"]
#EXPOSE 8080

# Image to build the project
FROM maven:3.8.4-openjdk-17 AS base

# Copy source code
COPY . /it-roast

# Building the Maven project
WORKDIR /it-roast
RUN mvn clean package -DskipTests

# Image for api module
FROM openjdk:17 AS builder
COPY --from=base /it-roast/target/*.jar /it-roast/it-roast.jar

# Image for combining modules
FROM openjdk:17
COPY --from=base /it-roast /it-roast
WORKDIR /it-roast
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "it-roast.jar"]
