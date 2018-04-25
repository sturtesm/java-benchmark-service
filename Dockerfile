FROM tomcat:7

MAINTAINER steve.sturtevant@appdynamics.com

ADD target/java-benchmark.war /usr/local/tomcat/webapps/java-benchmark.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
