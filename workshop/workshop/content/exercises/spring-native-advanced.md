

```terminal:execute
command: |
  cd spring-boot-hello-world
  ./mvnw clean package -DskipTests -Pnative
  cd ..
clear: true
```

```terminal:execute
command: ./spring-boot-hello-world/target/hello-world 
clear: true
```

```execute-2
curl http://localhost:8080
```

```editor:select-matching-text
file: spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldApplication.java
text: "@SpringBootApplication"
```


```editor:replace-text-selection
file: spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldApplication.java
text: |
  import org.springframework.nativex.hint.ResourceHint;

  @ResourceHint(
          patterns = {
                  "src/data/hello.*"
          }
  )
  @SpringBootApplication
```

```terminal:execute
command: |
  cd spring-boot-hello-world
  ./mvnw package -DskipTests -Pnative
  cd ..
clear: true
```

```terminal:execute
command: ./spring-boot-hello-world/target/hello-world 
clear: true
```

```execute-2
curl http://localhost:8080
```