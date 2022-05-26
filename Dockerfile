FROM maven:3-openjdk-17 AS MAVEN

MAINTAINER Antoine PRONNIER, <antoine.pronnier@gmail.com>

WORKDIR /container/funix-api/

COPY pom.xml .

COPY api/pom.xml ./api/
COPY api/src ./api/src

COPY core/pom.xml ./core/
COPY core/key.jks ./core/
COPY core/src ./core/src

COPY service/pom.xml ./service/
COPY service/src ./service/src

RUN mvn clean package -Dspring-boot.run.profiles=docker

FROM openjdk:17 AS FINAL

WORKDIR /container/java

COPY --from=MAVEN /container/funix-api/service/target/funix-api-server-*.jar /container/java/server.jar

ENTRYPOINT ["java", "-jar", "/container/java/server.jar", "-Dspring.profiles.active=docker"]
