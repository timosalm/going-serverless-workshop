
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

```execute-2
curl http://localhost:8080
```

```editor:select-matching-text
file: going-serverless-workshop/samples/spring-native-accessing-resources/src/main/java/com/example/springnativeaccessingresources/SpringNativeAccessingResourcesApplication.java
text: "public static void main(String[] args) {"
```


```editor:replace-text-selection
file: going-serverless-workshop/samples/spring-native-accessing-resources/src/main/java/com/example/springnativeaccessingresources/SpringNativeAccessingResourcesApplication.java
text: |
 	  @ResourceHint(
				patterns = {
							"src/data/test.*"
	  		}
	  )
	  public static void main(String[] args) {
```

```terminal:execute
command: |
  cd going-serverless-workshop/samples/spring-native-accessing-resources
  ./mvnw package -DskipTests -Pnative
  cd ..
clear: true
```

```terminal:execute
command: ./going-serverless-workshop/samples/spring-native-accessing-resources/target/spring-native-accessing-resources
clear: true
```

```execute-2
curl http://localhost:8080
```