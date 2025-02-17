FROM amazoncorretto:21
COPY target/*.jar ewmServiceApp.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /ewmServiceApp.jar"]