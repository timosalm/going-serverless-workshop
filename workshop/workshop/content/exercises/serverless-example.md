Let’s now find out how a typical Sping Boot application performs on a Serverless runtime like Knative.
```terminal:execute
command: git clone https://github.com/tsalm-pivotal/spring-boot-hello-world.git
clear: true
```

```terminal:execute
command: |
  cd spring-boot-hello-world
  ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-{{ session_namespace }} -DskipTests
  cd ..
clear: true
```

```terminal:execute
command: docker images | grep hello-world
clear: true
```

```terminal:execute
command: docker push harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-{{ session_namespace }}
clear: false
```

```terminal:execute
command: kn service create spring-boot-hello-world --image harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-{{ session_namespace }} --scale-min 1
clear: true
```

```terminal:execute
command: curl https://spring-boot-hello-world-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```

Hint: The activator component stalls the HTTP request until a user Pod is available to handle it.

```terminal:execute
command: k logs -l app=spring-boot-hello-world-00001 -c user-container | grep "Started HelloWorldApplication"
clear: true
```

```terminal:execute
command: hey -z 60s -c 1000 -m GET https://spring-boot-hello-world-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```
```execute-2
watch kubectl get pods
```

```terminal:execute
command: k top pods -l app=spring-boot-hello-world-00001 --containers | grep user-container
clear: true
```

```terminal:execute
command: dive harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-{{ session_namespace }}
clear: true
```