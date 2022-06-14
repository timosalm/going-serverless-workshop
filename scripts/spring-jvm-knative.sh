#!/bin/bash

set -e
set -u
set -o pipefail

cd samples/spring-boot-hello-world
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=harbor.emea.end2end.link/tap-workshop-examples/spring-boot-hello-world-graalvm-local -DskipTests
docker push harbor.emea.end2end.link/tap-workshop-examples/spring-boot-hello-world-graalvm-local
kn service create spring-boot-hello-world --image harbor.emea.end2end.link/tap-workshop-examples/spring-boot-hello-world-graalvm-local --scale-min 1 -n dev-space
cd ../..
curl https://spring-boot-hello-world-dev-space.cnr.emea.end2end.link
k logs -l app=spring-boot-hello-world-00001 -c user-container -n dev-space | grep "Started HelloWorldApplication"

k top pods -l app=spring-boot-hello-world-00001 --containers

hey -z 60s -c 1000 -m GET https://spring-boot-hello-world-graalvm-w02-s012.cnr.emea.end2end.link



