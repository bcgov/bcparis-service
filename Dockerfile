FROM openjdk:8-jre-slim

COPY ./target/bcparis-service-1.3.1.jar bcparis-service-1.3.1.jar

ENTRYPOINT ["java", "-jar","/bcparis-service-1.3.1.jar"]
