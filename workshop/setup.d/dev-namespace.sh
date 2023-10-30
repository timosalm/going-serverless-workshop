#!/bin/bash
set -x
set +e

kubectl patch serviceaccount default -p '{"secrets": [{"name": "educates-registry-credentials"}],"imagePullSecrets": [{"name": "educates-registry-credentials"}]}'
