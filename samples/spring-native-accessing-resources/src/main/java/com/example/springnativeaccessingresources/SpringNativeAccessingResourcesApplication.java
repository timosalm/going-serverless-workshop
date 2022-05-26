package com.example.springnativeaccessingresources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileCopyUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;


@SpringBootApplication
public class SpringNativeAccessingResourcesApplication {

	public static void main(String[] args) {
 		try (InputStream inputStream = SpringNativeAccessingResourcesApplication.class
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
