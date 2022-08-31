FROM openjdk:17-alpine
EXPOSE 8090
ADD target/irembo-back.jar irembo-back.jar
ENTRYPOINT ["java","-jar", "/irembo-back.jar"]