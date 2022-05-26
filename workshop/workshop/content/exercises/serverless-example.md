Let’s now find out how a typical Sping Boot application performs on a Serverless runtime like Knative.
```terminal:execute
command: git clone https://github.com/tsalm-pivotal/spring-boot-hello-world.git
clear: true
```
To run our sample application on top of Knative we first have to build a container. 
Spring Boot version 2.3.0 introduced **Cloud Native Buildpack** support to simplify the container image creation.

Buildpacks were first conceived by Heroku in 2011. Since then, they have been adopted by Cloud Foundry and other PaaS.
And the new generation of buildpacks, the [Cloud Native Buildpacks](https://buildpacks.io), is an incubating project in the CNCF which was initiated by Pivotal and Heroku in 2018.

Cloud Native Buildpacks (CNBs) **detect what is needed to compile and run an application** based on the application's source code.
The application is then **compiled by the appropriate buildpack and a container image with best practices in mind is build** with the runtime environment.

The biggest benefits of CNBs are **increased security, minimized risk, and increased developer productivity** because they don't need to care much about the details of how to build a container.

With Spring Boot 2.3 and later you can create a container image using the open source [Paketo Buildpacks](https://paketo.io) with the following commands for Maven ...
```terminal:execute
command: |
  cd spring-boot-hello-world
  ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-{{ session_namespace }} -DskipTests
  cd ..
clear: true
```

We can then have a look at the created container in our local Docker environment ...
```terminal:execute
command: docker images | grep hello-world
clear: true
```

... and push it to a remote container registry to make it consumable by our Kubernetes cluster.
```terminal:execute
command: docker push harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-{{ session_namespace }}
clear: false
```

Let's now deploy our application with Knative.
```terminal:execute
command: kn service create spring-boot-hello-world --image harbor.emea.end2end.link/spring-io-2022/spring-boot-hello-world-{{ session_namespace }} --scale-min 1
clear: true
```

As soon as the application is running, we are able to call it via the auto-generated Ingress.
```terminal:execute
command: curl https://spring-boot-hello-world-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```

If we have a look at the application's logs, we can see how long it took until the application was started. Remember this number as a reference for later.
```terminal:execute
command: k logs -l app=spring-boot-hello-world-00001 -c user-container | grep "Started HelloWorldApplication"
clear: true
```

Now it's time to check out the auto-scaling capabilities of Knative by executing the following to commands. You should be able to see how the number of pods will be scaled up based on the generated traffic with the `hey` tool.
```execute-2
watch kubectl get pods
```
```terminal:execute
command: hey -z 60s -c 1000 -m GET https://spring-boot-hello-world-{{session_namespace}}.{{ ENV_TAP_CNRS_SUBDOMAIN }}
clear: true
```
Exit both commands with `ctrl + c` and take a closer look at some other performance metrics. Also remember the CPU and memory consumption for later reference.
```terminal:execute
command: k top pods -l app=spring-boot-hello-world-00001 --containers | grep user-container
clear: true
```