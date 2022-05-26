package com.example.springnativereflection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* You can specify a TypeHint directly */
//@TypeHint(typeNames = {"com.example.StringReverser"})
//@TypeHint(typeNames = {"com.example.StringCapitalizer"})
//@TypeHint(typeNames = {"org.springframework.context.annotation.ProfileCondition"})

/* ... or more specific to the type and down to the method you need to leverage hints for */
//@NativeHint(
//	types = {
//		@TypeHint(types = {
//			com.example.StringReverser.class,
//			com.example.StringCapitalizer.class,
//			org.springframework.context.annotation.ProfileCondition
//		}, access = AccessBits.DECLARED_METHODS)
//	}
//)
@SpringBootApplication
public class SpringNativeReflectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNativeReflectionApplication.class, args);
	}

}
