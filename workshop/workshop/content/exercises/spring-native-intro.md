Spring Native provides incubating support for compiling Spring applications to lightweight native executables using the GraalVM native-image compiler.

The goal is to support compilation of existing or new Spring Boot applications to native executables - Unchanged!


You can get started with Spring Native very easy by using start.spring.io to create a new project.


```execute
sdk use java 22.1.0.r17-grl
```

```terminal:execute
command: |
  cd spring-boot-hello-world
  ./mvnw -Pnative -DskipTests package 
  cd ..
clear: true
```


Let's now see how our Spring Boot sample application performs as native image on a Serverless runtime!
```
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }} -Pnative-image -DskipTests
```

```terminal:execute
command: |
  kp image create spring-boot-hello-world-native --tag harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }} --local-path spring-boot-hello-world --env BP_NATIVE_IMAGE=true --wait
clear: true
```

```terminal:execute
command: kn service create spring-boot-hello-world-native --image harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }} --scale-min 1
clear: true
```

```terminal:execute
command: curl https://spring-boot-hello-world-native-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```

```terminal:execute
command: k logs -l app=spring-boot-hello-world-native-00001 -c user-container | grep "Started HelloWorldApplication"
clear: true
```

```terminal:execute
command: hey -z 60s -c 1000 -m GET https://spring-boot-hello-world-native-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```
```execute-2
watch kubectl get pods
```

```terminal:execute
command: k top pods -l app=spring-boot-hello-world-native-00001 --containers  | grep user-container
clear: true
```

```terminal:execute
command: dive harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }}
clear: true
```