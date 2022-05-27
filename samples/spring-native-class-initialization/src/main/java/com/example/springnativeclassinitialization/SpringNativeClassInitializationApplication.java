package com.example.springnativeclassinitialization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@NativeHint(
  initialization = @InitializationHint(types = {
    com.example.First.class,
		com.example.Second.class
}, initTime = InitializationTime.BUILD))
@SpringBootApplication
public class SpringNativeClassInitializationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNativeClassInitializationApplication.class, args);
	}

}
