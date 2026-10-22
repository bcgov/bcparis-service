FROM eclipse-temurin:8-jre-jammy

# Apply pending Ubuntu security updates (e.g. libgnutls30, libgcrypt20) that are not yet in the base image
RUN apt-get update \
    && apt-get upgrade -y --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

COPY ./target/bcparis-service-1.3.2.jar bcparis-service-1.3.2.jar

ENTRYPOINT ["java", "-jar","/bcparis-service-1.3.2.jar"]
