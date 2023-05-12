#!/bin/bash
set -x
set +e

echo $CONTAINER_REGISTRY_PASSWORD | docker login $CONTAINER_REGISTRY_HOSTNAME -u $CONTAINER_REGISTRY_USERNAME --password-stdin

kubectl patch serviceaccount default -p '{"imagePullSecrets": [{"name": "registry-credentials"}]}'