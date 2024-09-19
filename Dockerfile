FROM eclipse-temurin:21-jre-alpine

WORKDIR .
ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} app.jar

VOLUME /tmp
EXPOSE 8080

# k8s deployment could provide the command to overwrite this
# CMD ["sh","-c","java ${JAVA_OPTS} -jar app.jar"]
ENTRYPOINT ["sh", "-c", "java --add-opens java.base/sun.nio.ch=ALL-UNNAMED ${JAVA_OPTS} -jar app.jar"]
