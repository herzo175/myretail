.PHONY: build,test

DOCKER_TAG=jeremyaherzog/myretail:latest

export DOCKER_TAG

build:
	./gradlew build
	docker build . -t ${DOCKER_TAG}

test:
	./gradlew test

run:
	./gradlew bootRun

services:
	docker-compose up -d db

up:
	docker-compose up

down:
	docker-compose down