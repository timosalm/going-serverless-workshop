package com.example.springnativeclassinitialization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.InitializationHint;
import org.springframework.nativex.hint.InitializationTime;
import org.springframework.nativex.hint.NativeHint;

@NativeHint(
  initialization = @InitializationHint(types = {
    com.example.springnativeclassinitialization.First.class,
		com.example.springnativeclassinitialization.Second.class
}, initTime = InitializationTime.BUILD))
@SpringBootApplication
public class SpringNativeClassInitializationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassInit.class, args);
	}

}
