# Overview

## Building

Leverages the [docker-maven-plugin](https://github.com/spotify/docker-maven-plugin) to build a docker image that builds a docker image from our java package.

to build, run `mvn package -DskipTests=true`, remove the skipTests property if you want to run the unit tests.

## Core Dockerfile

Found in the <b>/docker</b> sub-folder

	FROM tomee:8-jre-1.7.4-jaxrs
	adds java payments web service to docker container via
	ADD devops-payments-web.war /usr/local/tomee/webapps/devops-payments-web.war

## Image

Running the `maven package` goal produces a new docker image:

	devops-demo/devops-payments-web        latest              3126df0d21e7        12 seconds ago      352.8 MB

## Running the container

The container can be run without much parameteriztion.  The tomcat instance is currently configured to listen on port 8080, so we want to map the container port 8080 to something available on our host system.  In this case we map to the host port 8888.

	docker run -it --rm -p 8888:8080 devops-demo/devops-payments-web

## Hitting the application

	http://127.0.0.1:8888/devops-payments-web/




 
