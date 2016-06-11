#!/bin/bash

docker stop play-mongo
docker stop play-mongo2
docker stop play-mongo3

docker rm play-mongo
docker rm play-mongo2
docker rm play-mongo3