

```editor:append-lines-to-file
file: ~/spring-boot-hello-world/src/data/hello.txt
text: Hello Resource File
```

```editor:select-matching-text
file: spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldResource.java
text: |4
  @GetMapping
      public ResponseEntity<String> fetchGreeting() {
          return ResponseEntity.ok("Hello from VMware Tanzu!");
      }
```
```editor:replace-text-selection
file: spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldResource.java
text: |4
  @GetMapping
      public ResponseEntity<String> fetchGreeting() throws IOException {
          // read properties
          InputStream inputStream = ResourceAccess.class
                  .getClassLoader()
                  .getResourceAsStream("src/data/hello.txt");
          Reader reader = new InputStreamReader(inputStream, UTF_8);
          String greeting = FileCopyUtils.copyToString(reader);
          return ResponseEntity.ok(greeting);
      }
```

```editor:select-matching-text
file: spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldResource.java
text: import org.springframework.web.bind.annotation.RestController;
```
```editor:replace-text-selection
file: spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldResource.java
text: |
  import org.springframework.web.bind.annotation.RestController;
  import org.springframework.util.FileCopyUtils;
  import java.io.IOException;
  import java.io.InputStream;
  import java.io.InputStreamReader;
  import java.io.Reader;
```

```terminal:execute
command: |
  cd spring-boot-hello-world
  ./mvnw clean package -DskipTests -Pnative
  cd ..
clear: true
```

```terminal:execute
command: ./spring-boot-hello-world/target/hello-world 
```

```execute-2
curl http://localhost:8080
```

```editor:select-matching-text
file: spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldApplication.java
text: @SpringBootApplication
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