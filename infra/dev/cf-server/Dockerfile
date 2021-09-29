FROM openjdk:16

EXPOSE 8080

RUN mkdir -p /home/user1

# application.properties files for api server
COPY application.yml /home/user1/application.yml

WORKDIR /home/user1
ENTRYPOINT java -jar /home/user1/cordfield.jar --spring.config.location=/home/user1/application.yml

# cordfield.jar 
COPY cordfield.jar /home/user1/cordfield.jar