#!/bin/bash

docker run --cpuset-cpus="0" -m 1g \
-v /home/vm/Escritorio/arqsoft2-tp/target/build-dev:/opt/app \
-v /home/vm/Escritorio/arqsoft2-tp/newrelic:/opt/newrelic \
-p 9200:9000 --name play-server --link play-mongo:mongo \
-h server.play --rm -it beevelop/java:latest /bin/bash