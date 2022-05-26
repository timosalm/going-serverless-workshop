```terminal:execute
command: |
  mkdir spring-boot-hello-world/src/data
  cat <<EOF>  ~/spring-boot-hello-world/src/data/hello.txt
  Hello Resource File
  EOF

  cat << EOF>  ~/spring-boot-hello-world/src/main/java/com/example/helloworld/HelloWorldResource.java
  package com.example.helloworld;

  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RestController;
  import org.springframework.util.FileCopyUtils;
  import java.io.IOException;
  import java.io.InputStream;
  import java.io.InputStreamReader;
  import java.io.Reader;

  import static java.nio.charset.StandardCharsets.UTF_8;

  @RestController
  public class HelloWorldResource {

      @GetMapping
      public ResponseEntity<String> fetchGreeting() throws IOException {
          InputStream inputStream = HelloWorldResource.class
                  .getClassLoader()
                  .getResourceAsStream("src/data/hello.txt");
          Reader reader = new InputStreamReader(inputStream, UTF_8);
          String greeting = FileCopyUtils.copyToString(reader);
          return ResponseEntity.ok(greeting);
      }
  }
  EOF
clear: true
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