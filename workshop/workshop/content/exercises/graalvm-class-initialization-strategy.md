It is very important for us to understand one of the most misunderstood features of native image: the class initialization strategy.

Classes in an application need to be initialized before being used. The lifecycle of the native image is split into two parts: build time and run time.

By default, classes are initialized at runtime. Sometimes it makes sense for optimization or other pourposes to initialize some classes at build time.

Let's explore an example application consisting of a few classes in order to:

get a better understanding of the implications of initialization at runtime or build time
how to configure the initialization strategy

```editor:append-lines-to-file
file: ~/ClassInit.java 
text: |
  import java.nio.charset.*;

  public class ClassInit {
      public static void main(String[] args) {
          First.second.printIt();
      }
  }

  class First {
      public static Second second = new Second();
  }

  class Second {
      private static final Charset UTF_32_LE = Charset.forName("UTF-32LE");

      public void printIt() {
          System.out.println("Unicode 32 bit CharSet: " + UTF_32_LE);
      }
  }
```

```terminal:execute
command: |
  javac ClassInit.java 
  java ClassInit
clear: true
```


Let's build a native image out of it:
```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image -cp . ClassInit
  ./classinit
clear: true
```

```terminal:execute
command: $GRAALVM_HOME/bin/native-image -H:+PrintClassInitialization ClassInit
clear: true
```
 
 ```terminal:execute
command: |
  $GRAALVM_HOME/bin/native-image  -H:+PrintClassInitialization --initialize-at-build-time=First,Second ClassInit
  ./classinit
clear: true
```

```editor:select-matching-text
file: ClassInit.java
text: "public static Second second = new Second();"
```
```editor:replace-text-selection
file: ClassInit.java
text: |2
  public static Second second = new Second();

    // part 2 of the exercise
    public static Thread t;

    static {
        t = new Thread(()-> {
            try {
                System.out.println("Sleep for 10s...");
                Thread.sleep(10_000);
                System.out.println("Done...");
            } catch (Exception e){}
        });
        t.start();
    } 
```

***TODO: https://github.com/ddobrin/native-spring-on-k8s-with-graalvm-workshop/blob/main/graalvm/class-initialization/README.md***


    public static Second second = new Second();

    // part 2 of the exercise
    public static Thread t;

    static {
        t = new Thread(()-> {
            try {
                System.out.println("Sleep for 10s...");
                Thread.sleep(10_000);
                System.out.println("Done...");
            } catch (Exception e){}
        });
        t.start();
    } 