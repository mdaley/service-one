FROM openjdk:alpine

ADD ./target/*-standalone.jar /service.jar

EXPOSE 8080
CMD ["java", "-jar", "/service.jar"]
