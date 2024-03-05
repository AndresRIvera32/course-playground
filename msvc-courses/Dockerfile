# we create our image based on a image base
FROM openjdk:17-jdk-alpine as builder

# root directory where we are going to work on
WORKDIR /app/msvc-courses

# we copy the pom.xml file into /app directory
COPY ./pom.xml /app
COPY ./msvc-courses/.mvn ./.mvn
COPY ./msvc-courses/mvnw .
COPY ./msvc-courses/pom.xml .

# this command will compile the pom.xml and will skip the test
# RUN will be executed downloading dependencies when the image is being build
#-Dmaven.test.skip this instruction will skip the test and compilation layer
#-Dmaven.main.skip this instruction will skip all compilation related to main package
#-Dspring-boot.repackage.skip will avoid the repackage instruction
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
RUN ./mvnw dependency:go-offline


# we copy the project /msvc-courses/src folder into /app/msvc-courses/src root image route
COPY ./msvc-courses/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /maindir
RUN mkdir ./logs

COPY --from=builder /app/msvc-courses/target/msvc-courses-0.0.1-SNAPSHOT.jar .

ENV DB_HOST postgresdb:5432

# we expose the port where the application is running as informative
EXPOSE 8002

#this is the entrypoint where the container will run
# we can use "ENTRYPOINT" or "CMD"
# CMD is mutable it means that it can be replaced in execution time of the container
# docker build -t courses . -f .\msvc-courses\Dockerfile -it dockerImageName /bin/sh
# ENTRYPOINT is inmutable which means it cannot be rewrited in execution time of the container
CMD ["java", "-jar", "msvc-courses-0.0.1-SNAPSHOT.jar"]
# CMD sleep 10 && java -jar msvc-courses-0.0.1-SNAPSHOT.jar

#if we need to execute this dockerfile we have to launch it from the source "course-playground"
#we can execute in main project using the following command: docker build -t courses . -f .\msvc-courses\Dockerfile