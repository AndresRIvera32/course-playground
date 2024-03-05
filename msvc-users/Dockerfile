#IMPORTANT
#WE HAVE TO CREATE THE NETWORK TO HAVE ALL SERVICES WORKING
#COMMAND: docker network create name-network

# we create our image based on a image base
FROM openjdk:17-jdk-alpine as builder

#if we want to use variables in the docker file we can use this
ARG MSVC_NAME=msvc-users

# root directory where we are going to work on
WORKDIR /app/$MSVC_NAME

# we copy the pom.xml file into /app directory
COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

# this command will compile the pom.xml and will skip the test
# RUN will be executed downloading dependencies when the image is being build
#-Dmaven.test.skip this instruction will skip the test and compilation layer
#-Dmaven.main.skip this instruction will skip all compilation related to main package
#-Dspring-boot.repackage.skip will avoid the repackage instruction
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline - this line will avoid to download some dependencies to speed up the image creation


# we copy the project /msvc-users/src folder into /app/msvc-users/src root image route
COPY ./msvc-users/src ./src

RUN ./mvnw clean package -DskipTests

#second layer of the image which will be based on the previous image
FROM openjdk:17-jdk-alpine

WORKDIR /maindir
#we can copy the logs and extract it to analyze
RUN mkdir ./logs

COPY --from=builder /app/msvc-users/target/msvc-users-0.0.1-SNAPSHOT.jar .

#Environment variables WE WILL KEEP THE SAME
#ENV PORT 8001
#ENV DB_HOST mysqldb:3306
#ENV DB_USERNAME root
#ENV DB_PASSWORD sasa
#ENV COURSES_URL msvc-courses:8002

# we expose the port where the application is running as informative
EXPOSE $PORT

#this is the entrypoint where the container will run
# we can use "ENTRYPOINT" or "CMD"
# CMD is mutable it means that it can be replaced in execution time of the container
# docker build -t users . -f .\msvc-users\Dockerfile -it dockerImageName /bin/sh
# ENTRYPOINT is inmutable which means it cannot be rewrited in execution time of the container
CMD ["java", "-jar", "msvc-users-0.0.1-SNAPSHOT.jar"]
#this will add 20 seconds waiting the resources in background to stay launched
#CMD sleep 20 && java -jar msvc-users-0.0.1-SNAPSHOT.jar

#if we need to execute this dockerfile we have to launch it from the source "course-playground"
#we can execute this project using the following command: docker build -t users . -f .\msvc-users\Dockerfile

#users will be the name that we use to create the image
#if we want to run the container we can execute docker run -d -p 8001:8001 --name msvc-users --network spring users
#if we want to use environment variables: docker run -d -p 8001:8001 -e PORT=8001 --name msvc-users --network spring users
#if we want to use a env file: docker run -d -p 8001:8001 --env-file .\msvc-users\.env --name msvc-users --network spring users
