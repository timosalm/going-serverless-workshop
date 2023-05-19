#!/bin/bash

set -e
set -u
set -o pipefail

cd samples/spring-boot-hello-world
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=<container-registry>/tap-workshop-examples/spring-boot-hello-world-graalvm-local -DskipTests
docker push <container-registry>/tap-workshop-examples/spring-boot-hello-world-graalvm-local
kn service create spring-boot-hello-world --image <container-registry>/tap-workshop-examples/spring-boot-hello-world-graalvm-local  -n default
cd ../..
curl https://spring-boot-hello-world-default.<env-url>
kubectl logs -l app=spring-boot-hello-world-00001 -c user-container -n default | grep "Started HelloWorldApplication"

kubectl top pods -l app=spring-boot-hello-world-00001 --containers

hey -z 60s -c 1000 -m GET https://spring-boot-hello-world-default.<env-url>
