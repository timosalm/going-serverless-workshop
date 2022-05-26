```terminal:execute
command: mkdir spring-boot-hello-world/src/data
clear: true
```

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
