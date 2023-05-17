package com.example.springnativeclassinitialization;

import java.nio.charset.*;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

@ImportRuntimeHints(ClassInit.ClassInitHints.class)
@Component
public class ClassInit implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        First.second.printIt();
    }

    static class ClassInitHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.
        }
    }
}

class First {
    public static Second second = new Second();
}

class Second {
    private static final Charset UTF_32_LE = Charset.forName("UTF-32LE");

    public void printIt() {
        System.out.println("Unicode 32 bit CharSet: " + UTF_32_LE);
    }
}

