package com.sean.cmm.plugin.elevator.cli.parser;


import com.sean.cmm.plugin.elevator.model.Elevator;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileParser {
    public static void parser(String fileName, Elevator elevator){
        InputStream inputStream = Elevator.class.getClassLoader()
                .getResourceAsStream(fileName);
        if (inputStream == null) {
            System.out.println("Could not find the resource file: " + fileName);
            return;
        }
        Path tempFile;
        try {
            tempFile = Files.createTempFile("temp", ".tmp");
            Files.copy(inputStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try (Stream<String> lines = Files.lines(tempFile, StandardCharsets.UTF_8)) {
            lines.forEach(line -> {
                System.out.println("$ " + line);
                LineParser.parse(line, elevator);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
    }
}
