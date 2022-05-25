#!/bin/bash
set -x
set +e

echo "IzDuyXNAmYRihn61vvM3rRVB75XQdE8A" | docker login harbor.emea.end2end.link -u "robot\$spring-io-2022+spring-io-attendees" --password-stdin
