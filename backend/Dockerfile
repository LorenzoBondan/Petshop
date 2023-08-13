FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/petshop-0.0.1-SNAPSHOT.jar petshop.jar
ENTRYPOINT ["java","-jar","/petshop.jar"]