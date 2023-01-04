FROM openjdk:18.0.2.1-slim
ARG JAR_FILE=back-end/target/back-end-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080