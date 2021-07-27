FROM openjdk:11-jdk-buster AS build
WORKDIR /build

RUN apt-get update && apt-get -y upgrade && apt-get -y install maven

COPY pom.xml pom.xml
RUN mvn dependency:resolve

COPY src src
RUN mvn package


FROM openjdk:11-jre-buster AS app
WORKDIR /app

# copy .war from build stage
COPY --from=build /build/target/*.war app.war

ENTRYPOINT ["java", "-jar", "app.war"]