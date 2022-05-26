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

  @RestController
  public class HelloWorldResource {

      @GetMapping
      public ResponseEntity<String> fetchGreeting() throws IOException {
          InputStream inputStream = ResourceAccess.class
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