**Spring Native provides incubating support for compiling Spring applications to lightweight Native Images using the GraalVM native-image compiler.**

The goal is to **support compilation of existing or new Spring Boot applications to Native Images - Unchanged!**

You can **get started** with Spring Native very **easy by using start.spring.io to create a new project**.

```editor:open-file
file: going-serverless-workshop/samples/spring-boot-hello-world.pom.xml
line: 1
```

```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-boot-hello-world
  ./mvnw -Pnative -DskipTests package 
  cd $HOME
clear: true
```

```terminal:execute
command: ./going-serverless-workshop/samples/spring-boot-hello-world/target/hello-world
clear: true
```

```execute-2
 curl http://localhost:8080
 ```


Let's now see how our Spring Boot sample application performs as native image on a Serverless runtime!
Due to the required resources to build the container image, instead of building it locally vai the following command ...
```
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }} -Pnative-image -DskipTests
```
... we'll delegate it to an external component running in the cluster, the VMware Tanzu Build Service which is also available as [kpack](https://github.com/pivotal/kpack) as open source software.
```terminal:execute
command: |
  kp image create spring-boot-hello-world-native --tag harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }} --local-path going-serverless-workshop/samples/spring-boot-hello-world/ --env BP_NATIVE_IMAGE=true --wait
clear: true
```
After the container is built, let's deploy our application with Knative and send a request as soon as it's running.
```terminal:execute
command: kn service create spring-boot-hello-world-native --image harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }} --scale-min 1
clear: true
```
```terminal:execute
command: curl https://spring-boot-hello-world-native-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```

We can also compare the startup time to the same application running on the JVM ...
```terminal:execute
command: k logs -l app=spring-boot-hello-world-native-00001 -c user-container | grep "Started HelloWorldApplication"
clear: true
```

... see how fast the number of pods will be scaled up.
```execute-2
watch kubectl get pods
```
```terminal:execute
command: hey -z 60s -c 1000 -m GET https://spring-boot-hello-world-native-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```

The memory and CPU consumptions is also dramatically reduced.
```terminal:execute
command: k top pods -l app=spring-boot-hello-world-native-00001 --containers  | grep user-container
clear: true
```

(Optional) Compare the different layer of both container images with the `dive` tool.
```terminal:execute
command: dive harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-native-{{ session_namespace }}
clear: true
```