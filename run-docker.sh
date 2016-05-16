#!/bin/bash

docker run -v /home/vm/Escritorio/arqsoft2-tp/target/build-dev:/opt/app -v /home/vm/Escritorio/arqsoft2-tp/newrelic:/opt/newrelic -p 9000:9000 --name play-server --link play-mongo:mongo --rm beevelop/java:latest /opt/app/bin/arqsof2-tp -J-javaagent:/opt/newrelic/newrelic.jar -Dnewrelic.config.file=./opt/newrelic/newrelic.yml -Dconfig.resource=application-docker.conf
