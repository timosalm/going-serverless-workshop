```terminal:interrupt
autostart: true
hidden: true
```
Let’s now build our first native image.

The **native image builder** or `native-image` is a utility that **processes all classes of an application and their dependencies**, including those from the JDK. It **statically analyzes** these data to determine **which classes and methods are reachable during the application execution**. Then it **ahead-of-time compiles** that reachable code and data **to a native executable for a specific operating system and architecture**. This entire process is called building an image (or the image build time) to clearly distinguish it from the compilation of Java source code to bytecode.

We will now compile the following example and build a native image via the `native-image` utility.
```editor:open-file
file: samples/graalvm/HelloWorld.java
line: 1
```

```terminal:execute
command: |
  cd samples/graalvm/
  javac HelloWorld.java
  $GRAALVM_HOME/bin/native-image HelloWorld
  ./helloworld
  cd $HOME
clear: true
```
In addition to the `native-image` utility, you can also use the **GraalVM native image plugin for Maven or Gradle** to build a native image.

In the following sections, we will now see how we can mitigate some of the Ahead-of-time compilation limitations.
