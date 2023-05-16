#!/bin/bash
set -x
set +e

jq ". + { \"editor.fontSize\": 14, \"redhat.telemetry.enabled\": false, \"vs-kubernetes.ignore-recommendations\": true, \"security.workspace.trust.startupPrompt\": \"never\"}" /home/eduk8s/.local/share/code-server/User/settings.json | sponge /home/eduk8s/.local/share/code-server/User/settings.json
