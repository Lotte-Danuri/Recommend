FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/Recommend-0.0.1-SNAPSHOT.jar RecommendServer.jar
ENTRYPOINT ["java","-jar","RecommendServer.jar"]