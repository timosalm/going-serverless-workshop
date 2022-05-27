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
As idividual classes, methods, and fields that should be accessible via reflection must be known to the native-image tool at build time, the executing of our Native Image fails with `java.lang.ClassNotFoundException` exeptions.

We will now use the tracing agent to write a configuration file that provide hints to the native image builder in terms of classes to be added. 
```terminal:execute
command: |
  sdk use java 22.1.0.r11-grl
  mkdir -p META-INF/native-image
  java -agentlib:native-image-agent=config-output-dir=META-INF/native-image Reflection StringReverser reverse "what is new"
  java -agentlib:native-image-agent=config-merge-dir=META-INF/native-image Reflection StringCapitalizer capitalize "what is new"
clear: true
```
```editor:open-file
file: going-serverless-workshop/samples/graalvm/META-INF/native-image/reflect-config.json
line: 1
```
Now we're able to rebuild and rerun our Native Image.
```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image --no-fallback Reflection
  ./reflection StringReverser reverse "what is new"
  ./reflection StringCapitalizer capitalize "what is new"
clear: true
```

Let's also compare the performance of the application running on the JDK vs as a Native Image.
```terminal:execute
command: |
  time java Reflection StringReverser reverse "what is new"
  time ./reflection StringReverser reverse "what is new"
clear: true
```

#### Accessing Resources
By default, the native-image tool will not integrate any of the resources that are on the classpath into the native executable. To make calls such as `Class.getResource()` or `Class.getResourceAsStream()` (or their corresponding ClassLoader methods) return specific resources (instead of null), you must specify the resources that should be accessible at runtime. This can also be achieved using a configuration file.

Let's first again run the following example on the JVM.
```editor:open-file
file: going-serverless-workshop/samples/graalvm/ResourceAccess.java
line: 1
```
```terminal:execute
command: |
  javac ResourceAccess.java 
  java ResourceAccess
clear: true
```

Let's build a native image out of it:
```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image --no-fallback ResourceAccess
  ./resourceaccess
clear: true
```
The execution breaks with an `NullPointerException` exception.

We can also use the **tracing agent** to write a configuration file. As an alternative individual resource paths can also be specified directly to native-image via the `-H:IncludeResources` flag.
```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image -H:IncludeResources=config.properties ResourceAccess
  ./resourceaccess
clear: true
```

#### Class Initialization
Classes in an application need to be initialized before being used. The lifecycle of the Native Image is split into two parts: **build time and run time**.

**By default, classes are initialized at runtime**. Sometimes it makes sense for **optimization or other purposes to initialize some classes at build time**.

Let's explore an example application consisting of a few classes in order to:
- get a better understanding of the implications of initialization at runtime or build time
- how to configure the initialization strategy

Let's first again run the following example on the JVM.
```editor:open-file
file: going-serverless-workshop/samples/graalvm/ClassInit.java
line: 1
```
```terminal:execute
command: |
  javac ClassInit.java 
  java ClassInit
clear: true
```

Let's build a native image out of it.
```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image --no-fallback ClassInit
  ./classinit
clear: true
```
The execution breaks with an `UnsupportedCharsetException` exception. At runtime, the platform initializes static fields. It calls the `Charset.forName()` methods. This works for UTF-8 and UTF-16LE because these charsets are available in the image. However, it does fail on the **UTF_32_LE** field because, as non-standard, it is **not by default included** in the Native Image and thus can't be found.

What we are interested in is to understand the class initialization details at this time.
For application classes, Native Image tries to find classes that can be safely initialized at build time. A class is considered safe if all of its relevant supertypes are safe and if the class initializer does not call any unsafe methods or initialize other unsafe classes.
The list of all classes that are proven safe is output to a file via the `-H:+PrintClassInitialization` command line argument to the native-image tool.
```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image -H:+PrintClassInitialization ClassInit
  tree reports/
  cat reports/class_initialization_report_20220527_082619.csv | grep UTF_8
  cat reports/class_initialization_report_20220527_082619.csv | grep UTF_16LE
  cat reports/class_initialization_report_20220527_082619.csv | grep UTF_32LE
clear: true
```

Two command line flags explicitly specify when a class should be initialized: `--initialize-at-build-time` and `--initialize-at-run-time`.
Let's now move the move the initialization to build-time. Then the Charset instance is written out to the image heap and can be used at runtime.
 ```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image  -H:+PrintClassInitialization --initialize-at-build-time=First,Second ClassInit
  ./classinit
clear: true
```

**Sometimes objects instantiated during the build class initialization cannot be written out and used at runtime.** Like for example opened files, running threads, opened network sockets
or random instances.

If we run the following example you should be able to observe that the running thread could not be written out, therefore we have incorrect behavior.
```editor:open-file
file: going-serverless-workshop/samples/graalvm/ClassInitThread.java
line: 1
```
 ```terminal:execute
command: |
  javac ClassInitThread.java 
  java ClassInitThread
  $GRAALVM_HOME/bin/native-image  -H:+PrintClassInitialization --initialize-at-build-time=First,Second ClassInitThread
  ./classinit 
clear: true
```

Balancing initialization can be a bit tricky, this is why by default GraalVM initializes classes at runtime.

So for this example to work correctly, we should have only Second to be initialized at build time.
 ```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image  -H:+PrintClassInitialization --initialize-at-build-time=Second ClassInitThread
  ./classinit 
  cd $HOME
clear: true
```

#### Dynamic Proxy and Serialization
Feel free to get the following examples working by yourself!
```editor:open-file
file: going-serverless-workshop/samples/graalvm/DynamicProxy.java
line: 1
```
```editor:open-file
file: going-serverless-workshop/samples/graalvm/Serialization.java
line: 1
```