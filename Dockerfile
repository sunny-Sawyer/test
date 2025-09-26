FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN apk add --update fontconfig ttf-dejavu
ADD Museum-0.0.1-SNAPSHOT.jar Museum.jar
EXPOSE 9999
ENTRYPOINT java -jar /app/Museum.jar