package com.example.springnativeaccessingresources;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootApplication
public class SpringNativeAccessingResourcesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringNativeAccessingResourcesApplication.class, args);
    }

}
