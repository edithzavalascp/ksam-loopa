FROM java:8
VOLUME /tmp
EXPOSE 8080
ADD /build/libs/ksam.jar ksam.jar
ENTRYPOINT ["java","-jar","ksam.jar"]
