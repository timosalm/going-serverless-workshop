package com.example.springnativereflection;

import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

class StringReverser {
    static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }
}

class StringCapitalizer {
    static String capitalize(String input) {
        return input.toUpperCase();
    }
}

@ImportRuntimeHints(Reflection.ReflectionRuntimeHints.class)
@Component
public class Reflection implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String className = args[0];
        String methodName = args[1];
        String input = args[2];

        Class<?> clazz = Class.forName(className);
        Method method = clazz.getDeclaredMethod(methodName, String.class);
        Object result = method.invoke(null, input);
        System.out.println(result);
    }

    static class ReflectionRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            var reverseMethod = ReflectionUtils.findMethod(StringReverser.class, "reverse", String.class);
            hints.reflection().registerMethod(reverseMethod, ExecutableMode.INVOKE);
            var capitalizeMethod = ReflectionUtils.findMethod(StringCapitalizer.class, "capitalize", String.class);
            hints.reflection().registerMethod(capitalizeMethod, ExecutableMode.INVOKE);
        }
    }
}