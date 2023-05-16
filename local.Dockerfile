FROM maven:3-openjdk-17 AS MAVEN

MAINTAINER Antoine PRONNIER, <antoine.pronnier@gmail.com>

WORKDIR /container/funixgaming-api/

COPY pom.xml .

COPY api/pom.xml ./api/
COPY api/src ./api/src

COPY service/pom.xml ./service/
COPY service/src ./service/src

RUN mvn clean package -B
RUN rm service/target/funixgaming-api-server-*-javadoc.jar
RUN rm service/target/funixgaming-api-server-*-sources.jar

FROM openjdk:17 AS FINAL

WORKDIR /container/java

COPY --from=MAVEN /container/funixgaming-api/service/target/funixgaming-api-server-*.jar /container/java/server.jar

ENTRYPOINT ["java", "-jar", "/container/java/server.jar", "-Dspring.profiles.active=docker"]