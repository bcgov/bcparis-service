FROM eclipse-temurin:17-jre-jammy

COPY ./target/bcparis-service-1.3.2.jar bcparis-service-1.3.2.jar

ENTRYPOINT ["java", "-jar","/bcparis-service-1.3.2.jar"]
