For GraalVM Native Images, **all the bytecode in the application needs to be observed and analysed at build time**.

One area the analysis process is responsible for, is to determine which classes, methods and fields need to be included in the executable. The **analysis is static**, so it might need some configuration to correctly include the parts of the program that use dynamic features of the language.

However, this analysis cannot always completely predict all usages of the **Java Reflection, Java Native Interface (JNI), Dynamic Proxy objects (java.lang.reflect.Proxy), or class path resources (Class.getResource)**. 

**Undetected usages of these dynamic features need to be provided to the native-image tool in the form of configuration files.**

In order to **make preparing these configuration files easier** and more convenient, **GraalVM provides an agent that tracks all usages of dynamic features of an execution on a regular Java VM**. 
During execution, the agent interfaces with the Java VM to intercept all calls that look up classes, methods, fields, resources, or request proxy accesses.

We will now have a look at several examples for different language features.

##### Reflection
If we run the following example on the JVM everything runs as expected via reflection.
```editor:open-file
file: going-serverless-workshop/samples/graalvm/Reflection.java
line: 1
```
```terminal:execute
command: |
  cd going-serverless-workshop/samples/graalvm/
  javac Reflection.java 
  java Reflection StringReverser reverse "what is new"
  java Reflection StringCapitalizer capitalize "what is new"
clear: true
```

Let's build a native image out of it.
```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image --no-fallback Reflection
  ./reflection StringReverser reverse "what is new"
  ./reflection StringCapitalizer capitalize "what is new"
clear: true
```
*Hint: Using `--no-fallback` option indicates to the native image that it should not revert and created a JVM image, in case the native image build would fail.*

To build a native image with reflective lookup operations, apply the tracing agent to write a configuration file to be later fed into the native image build together. The config file will provide hints to the native image builder in terms of classes to be added
```terminal:execute
command: |
  mkdir -p META-INF/native-image
  java -agentlib:native-image-agent=config-output-dir=META-INF/native-image Reflection StringReverser reverse "what is new"
  java -agentlib:native-image-agent=config-merge-dir=META-INF/native-image Reflection StringCapitalizer capitalize "what is new"
clear: true
```
```editor:open-file
file: META-INF/native-image/reflect-config.json
line: 1
```

```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image --no-fallback Reflection
  ./reflection StringReverser reverse "what is new"
  ./reflection StringCapitalizer capitalize "what is new"
clear: true
```


```terminal:execute
command: |
  time java Reflection StringReverser reverse "what is new"
  time ./reflection StringReverser reverse "what is new"
    cd $HOME
clear: true
```


https://www.graalvm.org/22.1/reference-manual/native-image/Limitations/

**TODO: https://github.com/ddobrin/native-spring-on-k8s-with-graalvm-workshop/blob/main/graalvm/assisted-configuration/README.md  https://www.graalvm.org/22.1/reference-manual/native-image/Limitations/ **
