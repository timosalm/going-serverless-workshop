
```editor:open-file
file: going-serverless-workshop/samples/spring-native-accessing-resources/src/main/java/com/example/springnativeaccessingresources/SpringNativeAccessingResourcesApplication.java
line: 1
```

```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-accessing-resources
  ./mvnw clean package -DskipTests -Pnative
  cd $HOME
clear: true
```

```terminal:execute
command: ./going-serverless-workshop/samples/spring-native-accessing-resources/target/spring-native-accessing-resources
clear: true
```

```editor:select-matching-text
file: going-serverless-workshop/samples/spring-native-accessing-resources/src/main/java/com/example/springnativeaccessingresources/SpringNativeAccessingResourcesApplication.java
text: "@SpringBootApplication"
```


```editor:replace-text-selection
file: going-serverless-workshop/samples/spring-native-accessing-resources/src/main/java/com/example/springnativeaccessingresources/SpringNativeAccessingResourcesApplication.java
text: |
  @ResourceHint(
				patterns = {
							"src/data/hello.*"
	  		}
	)
  @SpringBootApplication
```

```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-accessing-resources
  ./mvnw package -DskipTests -Pnative
   cd $HOME
clear: true
```

```terminal:execute
command: ./going-serverless-workshop/samples/spring-native-accessing-resources/target/spring-native-accessing-resources
clear: true
```