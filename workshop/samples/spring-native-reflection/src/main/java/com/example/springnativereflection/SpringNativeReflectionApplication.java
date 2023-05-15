package com.example.springnativereflection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(typeNames = {"com.example.springnativereflection.StringReverser"}, access = {TypeAccess.DECLARED_METHODS})
@TypeHint(typeNames = {"com.example.springnativereflection.StringCapitalizer"}, access = {TypeAccess.DECLARED_METHODS})
@SpringBootApplication
public class SpringNativeReflectionApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringNativeReflectionApplication.class, args);
  }

}
