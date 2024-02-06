FROM openjdk:18
WORKDIR /opt/securityAppBack1
COPY /target/*.jar securityListener.jar
ARG DEFAULT_PORT=8085
ENV PORT $DEFAULT_PORT
EXPOSE $PORT
ENTRYPOINT ["java", "-jar", "securityListener.jar"]