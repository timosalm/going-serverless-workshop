We'll now see how we can provide the configuration to the native-image tool that is needed for the undetected usages of dynamic language features with Spring Boot.

The `RuntimeHints API` collects the need for reflection, resource loading, serialization, and JDK proxies at runtime. 
Several contracts are handled automatically during AOT processing. For cases that the **core container cannot infer**, you can **register such hints programmatically** by implementing the `RuntimeHintsRegistrar` interface. Implementations of this interface can be registered using `@ImportRuntimeHints` on any Spring bean or @Bean factory method.

If you have **classes that need binding** (mostly needed when serializing or deserializing JSON), most of the hints are automatically inferred, for example when accepting or returning data from a @RestController method. But when you work with WebClient or RestTemplate directly, you might need to use `@RegisterReflectionForBinding` annotation.

##### Reflection
To bypass the class loading and reflection problems, we can add the hints in the following way.
```editor:open-file
file: samples/spring-native-reflection/src/main/java/com/example/springnativereflection/Reflection.java
line: 26
```
If at all possible, `@ImportRuntimeHints` should be used as close as possible to the component that requires the hints. This way, if the component is not contributed to the BeanFactory, the hints won’t be contributed either.

Before creating a native image, let's run the AOT compilation and observe that the correct configurations have been generated.
```terminal:execute
command: |
  cd samples/spring-native-reflection
  ./mvnw clean package -Pnative -DskipTests
  cat target/spring-aot/main/resources/META-INF/native-image/com.example/spring-native-reflection/reflect-config.json | grep 'StringReverser\|StringCapitalizer' -A 8 -B 1
  cd $HOME
clear: true
```

As this is the case, let's create the native image and run the application.
```terminal:execute
command: |
  cd samples/spring-native-reflection
  ./mvnw -Pnative -DskipTests native:compile 
  ./target/spring-native-reflection com.example.springnativereflection.StringReverser reverse "what is new"
  ./target/spring-native-reflection com.example.springnativereflection.StringCapitalizer capitalize "what is new"
  cd $HOME
clear: true
```

#### Accessing Resources
Resources that should be available on the classpath to be accessible at runtime can be specified in the same way.
```editor:open-file
file: samples/spring-native-accessing-resources/src/main/java/com/example/springnativeaccessingresources/ResourceAccess.java
line: 1
```

You can run the AOT compilation and observe that the correct configurations have been generated before we create the native image and run the application.
```terminal:execute
command: |
  cd samples/spring-native-accessing-resources
  ./mvnw clean package -Pnative -DskipTests
  cat target/spring-aot/main/resources/META-INF/native-image/com.example/spring-native-accessing-resources/resource-config.json | grep hello
  cd $HOME
clear: true
```

```terminal:execute
command: |
  cd samples/spring-native-accessing-resources
  ./mvnw -Pnative -DskipTests native:compile 
  ./target/spring-native-accessing-resources
  cd $HOME
clear: true
```
#### Class Initialization
As the Spring team recommends **limiting class initialization at build time** as much as possible due to side effects like for example static loggers and their configuration getting also initialized, there is on purpose **no way to provide those hints** in a programmatic or declarative way available **in Spring**. Providing hints for the code that will be executed on class initialization should fulfill the need for initialization at build time in most cases.

For those few cases where that's not the case like in our example, we can provide [native image build configuration](https://www.graalvm.org/22.3/reference-manual/native-image/overview/BuildConfiguration/) in `META-INF/native-image` (sub-)directories and use it **to construct native-image command-line options**.

To avoid a situation when constituent parts of a project are built with overlapping configurations, we recommended you use subdirectories within META-INF/native-image.

Let's have a look at the native image build configuration for our example.
```editor:open-file
file: samples/spring-native-class-initialization/src/main/resources/META-INF/native-image/custom/native-image.properties
line: 1
```

If we run the AOT compilation, we can have a look at the generated native image build configuration adding some defaults. As already mentioned, you should ensure that you don't override them by mistake by not adding your custom configuration to the `META-INF/native-image/<package>/<artifact>/native-image.properties` directory. 
```terminal:execute
command: |
  cd samples/spring-native-class-initialization
  ./mvnw clean package -Pnative -DskipTests
  cat target/spring-aot/main/resources/META-INF/native-image/com.example/spring-native-class-initialization/native-image.properties
  cd $HOME
clear: true
```

Now it's time that our application works like expected.
```terminal:execute
command: |
  cd samples/spring-native-class-initialization
  ./mvnw -Pnative -DskipTests native:compile 
  ./target/spring-native-class-initialization
  cd $HOME
clear: true
```

#### Dynamic Proxy and Serialization
Hints for the Dynamic Proxy and Serialization features can be provided in the same way via `hints.proxies().registerJdkProxy(MyInterface.class)` and `hints.serialization().registerType(MySerializableClass.class)`.

