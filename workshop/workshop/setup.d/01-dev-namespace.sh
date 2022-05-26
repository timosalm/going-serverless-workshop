#!/bin/bash
set -x
set +e

echo $CONTAINER_REGISTRY_PASSWORD | docker login $CONTAINER_REGISTRY_HOSTNAME -u $CONTAINER_REGISTRY_USERNAME --password-stdin

REGISTRY_PASSWORD=$CONTAINER_REGISTRY_PASSWORD kp secret create registry-credentials --registry ${CONTAINER_REGISTRY_HOSTNAME} --registry-user ${CONTAINER_REGISTRY_USERNAME}
kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "registry-credentials"}, {"name": "tanzu-net-credentials"}]}'

git clone https://github.com/tsalm-pivotal/going-serverless-workshop.git