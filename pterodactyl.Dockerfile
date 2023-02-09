FROM maven:3-openjdk-17 AS MAVEN

MAINTAINER Antoine PRONNIER, <antoine.pronnier@gmail.com>

WORKDIR /container/funix-api/

COPY pom.xml .

COPY api/pom.xml ./api/
COPY api/src ./api/src

COPY core/pom.xml ./core/
COPY core/src ./core/src

COPY service/pom.xml ./service/
COPY service/src ./service/src

RUN mvn clean package
RUN rm service/target/funix-api-server-*-javadoc.jar
RUN rm service/target/funix-api-server-*-sources.jar

FROM openjdk:17 AS FINAL_PTEROQ

MAINTAINER Antoine PRONNIER, <antoine.pronnier@gmail.com>

USER container
ENV USER=container HOME=/home/container
WORKDIR /home/container

COPY --from=MAVEN /container/funix-api/service/target/funix-api-server-*.jar /home/java/server.jar

COPY ./entrypointPteroq.sh /entrypoint.sh

CMD ["/bin/bash", "/entrypoint.sh"]
