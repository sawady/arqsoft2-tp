#!/bin/bash

docker stop play-mongo
docker stop play-mongo2
docker stop play-mongo3

docker rm play-mongo
docker rm play-mongo2
docker rm play-mongo3

export mongo1=172.17.0.2
export mongo2=172.17.0.3
export mongo3=172.17.0.4

docker run \
--hostname="mongo1.play.com" \
--add-host mongo1.play.com:${mongo1} \
--add-host mongo2.play.com:${mongo2} \
--add-host mongo3.play.com:${mongo3} \
-p 27017:27017 --name play-mongo -d mongo --replSet rs0

docker run \
--hostname="mongo2.play.com" \
--add-host mongo1.play.com:${mongo1} \
--add-host mongo2.play.com:${mongo2} \
--add-host mongo3.play.com:${mongo3} \
-p 27018:27017 --name play-mongo2 -d mongo --replSet rs0

docker run \
--hostname="mongo3.play.com" \
--add-host mongo1.play.com:${mongo1} \
--add-host mongo2.play.com:${mongo2} \
--add-host mongo3.play.com:${mongo3} \
-p 27019:27017 --name play-mongo3 -d mongo --replSet rs0