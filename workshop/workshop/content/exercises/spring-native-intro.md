**Spring Native provides incubating support for compiling Spring applications to lightweight Native Images using the GraalVM native-image compiler.**

The goal is to **support compilation of existing or new Spring Boot applications to Native Images - Unchanged!**

You can **get started** with Spring Native very **easy by using start.spring.io to create a new project**.

```editor:open-file
file: going-serverless-workshop/samples/spring-boot-hello-world/pom.xml
line: 1
```
As you can see, to use Spring Native for our example application, we added a dependency to the latest Spring Native library, configured the Paketo Java Native Image Buildpack in the `native` profile by setting `BP_NATIVE_IMAGE` to `true`.
We can also see that the Spring AOT plugin was added, which performs ahead-of-time transformations required to improve native image compatibility and footprint.
Last but not least for the second option to build your Native Image, the `native-maven-plugin` was added to our pom file to be able to invoke the native image compiler from your build.
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
Due to the required resources to build the container image, instead of building it locally via the following command ...
```
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName={{ ENV_CONTAINER_REGISTRY_HOSTNAME }}/{{ ENV_CONTAINER_REGISTRY_REPOSITORY }}/spring-boot-hello-world-native-{{ session_namespace }} -Pnative -DskipTests
```
... we'll delegate it to an external component running in the cluster, the VMware Tanzu Build Service which is also available as [kpack](https://github.com/pivotal/kpack) as open source software.
But let's first exit the running application with `ctrl + c`.
```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-boot-hello-world
  ./mvnw clean
  kp image create spring-boot-hello-world-native --tag {{ ENV_CONTAINER_REGISTRY_HOSTNAME }}/{{ ENV_CONTAINER_REGISTRY_REPOSITORY }}/spring-boot-hello-world-native-{{ session_namespace }} --local-path . --env BP_NATIVE_IMAGE=true --wait
  cd $HOME
clear: true
```
After the container is built, let's deploy our application with Knative and send a request as soon as it's running.
```terminal:execute
command: kn service create spring-boot-hello-world-native --image {{ ENV_CONTAINER_REGISTRY_HOSTNAME }}/{{ ENV_CONTAINER_REGISTRY_REPOSITORY }}/spring-boot-hello-world-native-{{ session_namespace }} --scale-min 1
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

The memory and CPU consumptions of the `user-container` is also dramatically reduced.
```terminal:execute
command: k top pods -l app=spring-boot-hello-world-native-00001 --containers
clear: true
```

(Optional) Compare the different layer of both container images with the `dive` tool.
```terminal:execute
command: dive {{ ENV_CONTAINER_REGISTRY_HOSTNAME }}/{{ ENV_CONTAINER_REGISTRY_REPOSITORY }}/spring-boot-hello-world-native-{{ session_namespace }}
clear: true
```
```terminal:execute
command: dive {{ ENV_CONTAINER_REGISTRY_HOSTNAME }}/{{ ENV_CONTAINER_REGISTRY_REPOSITORY }}/spring-boot-hello-world-{{ session_namespace }}
clear: true
```