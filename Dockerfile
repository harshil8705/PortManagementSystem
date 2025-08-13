FROM openjdk:26-oraclelinux9
WORKDIR /app

# Copy the wait-for-it script
COPY wait-for-it.sh .

# Copy your Spring Boot jar
COPY target/port-management-system.jar port-management-system.jar

# Set execute permission inside the container
RUN chmod +x wait-for-it.sh

EXPOSE 9090
