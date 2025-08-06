# syntax=docker/dockerfile:1.4
FROM gradle:8.8-jdk21 AS builder
WORKDIR /app

COPY . /app

RUN --mount=type=secret,id=gradle_props,dst=/root/.gradle/gradle.properties \
    gradle bootJar -x test --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/ReprocessarScanntech.jar

RUN mkdir /vr

EXPOSE 9011

CMD ["java","-Duser.timezone=America/Sao_Paulo","-jar","ReprocessarScanntech.jar"]
