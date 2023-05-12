#!/bin/bash
set -x
set +e

echo $CONTAINER_REGISTRY_PASSWORD | docker login $CONTAINER_REGISTRY_HOSTNAME -u $CONTAINER_REGISTRY_USERNAME --password-stdin

kubectl() {
    if [[ $@ == *"secret"* ]]; then
        command echo "No resources found in $SESSION_NAMESPACE namespace."
    else
        command kubectl "$@"
    fi
}

k() {
    if [[ $@ == *"secret"* ]]; then
        command echo "No resources found in $SESSION_NAMESPACE namespace."
    else
        command kubectl "$@"
    fi
}