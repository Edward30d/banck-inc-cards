FROM openjdk:17-jdk-alpine

LABEL maintainer="edward.loaiza30@gmail.com"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/banck-inc-cards-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} banck-inc-cards.jar

ENTRYPOINT ["java","-jar","/banck-inc-cards.jar"]