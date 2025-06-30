FROM openjdk:17-alpine

COPY . .

RUN apk add --update maven
RUN mvn clean package -Dmaven.test.skip=true

WORKDIR /app

RUN cp /target/votacao-1.0.jar /app/votacao.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "votacao.jar"]