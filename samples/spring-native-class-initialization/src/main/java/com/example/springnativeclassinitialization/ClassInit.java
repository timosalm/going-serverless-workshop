package com.example.springnativeclassinitialization;

import java.nio.charset.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ClassInit implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        First.second.printIt();
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

