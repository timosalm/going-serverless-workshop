#!/bin/bash
set -x
set +e


while (! docker stats --no-stream >/dev/null 2>&1); do
  echo "Waiting for Docker to launch..."
  sleep 1
done

docker pull docker.io/paketobuildpacks/builder:base