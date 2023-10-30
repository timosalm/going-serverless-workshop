package com.example.springnativeaccessingresources;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

@ImportRuntimeHints(ResourceAccess.ResourceAccessHints.class)
@Component
public class ResourceAccess implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        try (InputStream inputStream = ResourceAccess.class
                .getClassLoader()
                .getResourceAsStream("hello.txt")) {
            Reader reader = new InputStreamReader(inputStream, UTF_8);
            String greeting = FileCopyUtils.copyToString(reader);
            System.out.println(greeting);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ResourceAccessHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            hints.resources().registerPattern("hello.txt");
        }
    }
}
