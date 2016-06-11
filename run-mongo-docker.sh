#!/bin/bash

docker stop play-mongo
docker rm play-mongo
docker run -v /home/vm/mongo/data:/data/db --name play-mongo -d mongo
