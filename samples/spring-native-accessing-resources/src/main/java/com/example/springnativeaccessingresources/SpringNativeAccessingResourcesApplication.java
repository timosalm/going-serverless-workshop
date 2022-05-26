package com.example.springnativeaccessingresources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringNativeAccessingResourcesApplication {


	//@ResourceHint(
	//				patterns = {
	//								"src/data/test.*"
	//				}
	//)
	public static void main(String[] args) {
 		try (InputStream inputStream = HelloWorldResource.class
						.getClassLoader()
						.getResourceAsStream("src/data/hello.txt")) {
		Reader reader = new InputStreamReader(inputStream, UTF_8);
		String greeting = FileCopyUtils.copyToString(reader);
		System.out.println(greeting);
 		} catch (IOException e) {
        e.printStackTrace();
    }
	}

}
