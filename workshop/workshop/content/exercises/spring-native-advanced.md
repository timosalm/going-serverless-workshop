Let's now see how we can provide the configuration to the native-image tool that is needed for the undetected usages of dynamic languages features with Spring Native.

##### Reflection
To bypass the class loading and reflection problems, there is the `@TypeHint` annotation available, to indicate to the GraalVM compiler which configuration is required to be generated.
```editor:open-file
file: going-serverless-workshop/samples/spring-native-reflection/src/main/java/com/example/springnativereflection/SpringNativeReflectionApplication.java
line: 1
```
You can also specify the hint as part of a `@NativeHint` declaration and specify a finer-grained set of hints.
```
@NativeHint(
  types = {
    @TypeHint(types = {
      com.example.StringReverser.class,
      com.example.StringCapitalizer.class,
      org.springframework.context.annotation.ProfileCondition
    }, access = AccessBits.DECLARED_METHODS)
  }
)
```

You can run the Spring AOT plugin and observe that correct configurations have been generated, ...
```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-reflection
  ./mvnw clean package spring-aot:generate
  tree target/generated-sources/spring-aot/src/main/resources/
  cd $HOME
clear: true
```

... build the image and run the application.
```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-reflection
  ./mvnw -Pnative -DskipTests package 
  ./target/spring-native-reflection
  cd $HOME
clear: true
```

#### Accessing Resources
Spring AOT relies on the `@ResourceHint` annotation provided in SpringNativeAccessingResourcesApplication class to generate the proper configurations.
```editor:open-file
file: going-serverless-workshop/samples/spring-native-accessing-resources/src/main/java/com/example/springnativeaccessingresources/SpringNativeAccessingResourcesApplication.java
line: 1
```

You can run the Spring AOT plugin and observe that correct configurations have been generated, ...
```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-accessing-resources
  ./mvnw clean package spring-aot:generate
  tree target/generated-sources/spring-aot/src/main/resources/
  cd $HOME
clear: true
```

... build the image and run the application.
```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-accessing-resources
  ./mvnw -Pnative -DskipTests package 
  ./target/spring-native-accessing-resources
  cd $HOME
clear: true
```
#### Class Initialization
We can control the initialization with Spring Native with the `@NativeHint` declaration.
```editor:open-file
file: going-serverless-workshop/samples/spring-native-class-initialization/src/main/java/com/example/springnativeclassinitialization/SpringNativeClassInitializationApplication.java
line: 1
```

You can run the Spring AOT plugin and observe that correct configurations have been generated ...
```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-class-initialization
  ./mvnw clean package spring-aot:generate
  tree target/generated-sources/spring-aot/src/main/resources/
  cd $HOME
clear: true
```

... build the image and run the application.
```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-class-initialization
  ./mvnw -Pnative -DskipTests package 
  ./target/spring-native-class-initialization
  cd $HOME


#### Dynamic Proxy and Serialization
To indicate to the GraalVM compiler which configuration is required to be generated for the Dynamic Proxy and Serialization features, there are the `@JdkProxyHint` and  `@SerializationHint` annotations available.


