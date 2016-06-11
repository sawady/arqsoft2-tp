#!/bin/bash

export mongo1=172.17.0.2
export mongo2=172.17.0.3
export mongo3=172.17.0.4

docker run -v /home/vm/Escritorio/arqsoft2-tp/target/build-dev:/opt/app \
-v /home/vm/Escritorio/arqsoft2-tp/newrelic:/opt/newrelic -p 9000:9000 \
--name play-server \
-h server.play.com \
--add-host mongo1.play.com:${mongo1} \
--add-host mongo2.play.com:${mongo2} \
--add-host mongo3.play.com:${mongo3} \
--rm beevelop/java:latest /opt/app/bin/arqsof2-tp \
-J-javaagent:/opt/newrelic/newrelic.jar -Dnewrelic.config.file=./opt/newrelic/newrelic.yml \
-Dconfig.resource=application-docker.conf