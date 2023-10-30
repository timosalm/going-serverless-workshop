**Spring Boot 3 added support for compiling Spring applications to lightweight native images using the GraalVM native-image compiler.**

Spring Boot applications are typically dynamic and configuration is performed at runtime, but when creating native images with GraalVM, **a closed-world approach is used to retain static analysis benefits**. This means implies the following restrictions:
- **The classpath is fixed and fully defined at build time**
- The **beans defined in your application cannot change at runtime**. So the `@Profile` annotation, profile-specific configuration, and Properties that change if a bean is created are not supported

When these restrictions are in place, it becomes possible for Spring to perform ahead-of-time processing during build-time and generate additional assets that GraalVM can use. 

You can **get started** very **easily by using start.spring.io to create a new project**.

```editor:open-file
file: samples/spring-boot-hello-world/pom.xml
line: 1
```

The `spring-boot-starter-parent` declares a native profile that configures the executions that need to run to create a native image. You can activate profiles using the `-P` flag on the command line.

Spring Boot includes buildpack support for native images directly for both Maven and Gradle. The resulting image doesn’t contain a JVM, which leads to smaller images.

For the second option to build your native image, the `native-maven-plugin` was added to our pom file to be able to invoke the native image compiler from your build.
```terminal:execute
command: |
  cd samples/spring-boot-hello-world
  ./mvnw -Pnative -DskipTests native:compile 
  cd $HOME
clear: true
```

```terminal:execute
command: ./samples/spring-boot-hello-world/target/hello-world
clear: true
```

```execute-2
 curl http://localhost:8080
 ```


Let's now see how our Spring Boot sample application **performs as a native image on a Serverless runtime**!
Due to the required resources to build the container image, **instead of building it locally** via the following command ...
```
./mvnw package spring-boot:build-image -Dspring-boot.build-image.imageName={{ REGISTRY_HOST }}/spring-boot-hello-world-native-{{ session_namespace }} -Pnative -DskipTests
```
... we'll **delegate it to an external component running in the cluster**, the VMware Tanzu Build Service which is also available as [kpack](https://github.com/pivotal/kpack) as open source software.
But let's first exit the running application with `ctrl + c`.
```terminal:execute
command: |
  cd samples/spring-boot-hello-world
  ./mvnw clean
  kp image create spring-boot-hello-world-native --tag {{ REGISTRY_HOST }}/spring-boot-hello-world-native-{{ session_namespace }} --local-path . --env BP_NATIVE_IMAGE=true --env BP_JVM_VERSION=17 --env BP_MAVEN_BUILD_ARGUMENTS="-Dmaven.test.skip=true --no-transfer-progress package -Pnative" --env BP_NATIVE_IMAGE_BUILD_ARGUMENTS="--no-fallback" --wait
  cd $HOME
clear: true
```
After the container is built, let's deploy our application with Knative and send a request as soon as it's running.
```terminal:execute
command: kn service create spring-boot-hello-world-native --image {{ REGISTRY_HOST }}/spring-boot-hello-world-native-{{ session_namespace }} 
clear: true
```
```terminal:execute
command: curl https://spring-boot-hello-world-native-{{session_namespace}}.{{ ENV_TAP_INGRESS }}
clear: true
```

We can also compare the startup time to the same application running on the JVM ...
```terminal:execute
command: kubectl logs -l app=spring-boot-hello-world-native-00001 -c user-container | grep "Started HelloWorldApplication"
clear: true
```

... see how fast the number of pods will be scaled up.
```execute-2
watch kubectl get pods
```
```terminal:execute
command: hey -z 60s -c 1000 -m GET https://spring-boot-hello-world-native-{{session_namespace}}.{{ ENV_TAP_INGRESS }}
clear: true
```

The memory and CPU consumption of the `user-container` is also dramatically reduced.
```terminal:execute
command: kubectl top pods -l app=spring-boot-hello-world-native-00001 --containers
clear: true
```

(Optional) Compare the different layers of both container images with the `dive` tool.
```terminal:execute
command: dive {{ REGISTRY_HOST }}/spring-boot-hello-world-native-{{ session_namespace }}
clear: true
```
```terminal:execute
command: dive {{ REGISTRY_HOST }}/spring-boot-hello-world-{{ session_namespace }}
clear: true
```