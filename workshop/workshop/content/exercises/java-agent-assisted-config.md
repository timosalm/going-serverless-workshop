GraalVM native image build makes close world assumptions, which means all the bytecode in the application needs to be observed and analysed at build time.

One area the analysis process is responsible for is to determine which classes, methods and fields need to be included in the executable. The analysis is static, so it might need some configuration to correctly include the parts of the program that use dynamic features of the language.

However, this analysis cannot always completely predict all usages of the Java Reflection, Java Native Interface (JNI), Dynamic Proxy objects (java.lang.reflect.Proxy), or class path resources (Class.getResource). Undetected usages of these dynamic features need to be provided to the native-image tool in the form of configuration files.

In order to make preparing these configuration files easier and more convenient, GraalVM provides an agent that tracks all usages of dynamic features of an execution on a regular Java VM. 
During execution, the agent interfaces with the Java VM to intercept all calls that look up classes, methods, fields, resources, or request proxy accesses.

```editor:append-lines-to-file
file: ~/Reflection.java 
text: |
  import java.lang.reflect.Method;

  class StringReverser {
      static String reverse(String input) {
          return new StringBuilder(input).reverse().toString();
      }
  }

  class StringCapitalizer {
      static String capitalize(String input) {
          return input.toUpperCase();
      }
  }

  public class Reflection {
      public static void main(String[] args) throws ReflectiveOperationException {
          String className = args[0];
          String methodName = args[1];
          String input = args[2];

          Class<?> clazz = Class.forName(className);
          Method method = clazz.getDeclaredMethod(methodName, String.class);
          Object result = method.invoke(null, input);
          System.out.println(result);
      }
  }
```

```terminal:execute
command: |
  javac Reflection.java 
  java Reflection StringReverser reverse "what is new"
  java Reflection StringCapitalizer capitalize "what is new"
clear: true
```

As expected, the method reverseand ca[italize methods were found via reflection.

Let's build a native image out of it:
```terminal:execute
command: |
  native-image --no-fallback Reflection
  ./reflection StringReverser reverse "what is new"
clear: true
```
NOTE: Using `--no-fallback` option indicates to the native image that it should not revert and created a JVM image, in case the native image build would fail. Fallback images are regular JVM images which can be run using the usual java Reflection command.



**TODO: https://github.com/ddobrin/native-spring-on-k8s-with-graalvm-workshop/blob/main/graalvm/assisted-configuration/README.md**

