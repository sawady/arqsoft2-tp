#!/bin/bash

./activator clean dist

unzip ./target/universal/arqsof2-tp-*.zip

rm -rf ./target/build-dev && mv arqsof2-tp-*/ ./target/build-dev/
