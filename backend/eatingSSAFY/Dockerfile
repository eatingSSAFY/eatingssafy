FROM openjdk:17
ARG JAR_FILE=/backend/eatingSSAFY/build/libs/eatingSSAFY-1.0.jar
COPY ${JAR_FILE} app.jar
COPY /backend/eatingSSAFY/src/main/resources/fcm-service-account.json /tmp/fcm-service-account.json
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]