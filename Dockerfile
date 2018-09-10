FROM java:8

WORKDIR = /app

COPY target/csadmin-rest-service*.jar /app/app.jar

