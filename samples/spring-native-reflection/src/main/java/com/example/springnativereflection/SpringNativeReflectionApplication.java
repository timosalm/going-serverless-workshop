package com.example.springnativereflection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(typeNames = {"com.example.springnativereflection.StringReverser"})
@TypeHint(typeNames = {"com.example.springnativereflection.StringCapitalizer"})
@TypeHint(typeNames = {"org.springframework.context.annotation.ProfileCondition"})
@SpringBootApplication
public class SpringNativeReflectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNativeReflectionApplication.class, args);
	}

}
