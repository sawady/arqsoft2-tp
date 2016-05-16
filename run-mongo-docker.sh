#!/bin/bash

docker stop play-mongo
docker rm play-mongo
docker run --name play-mongo -d mongo
