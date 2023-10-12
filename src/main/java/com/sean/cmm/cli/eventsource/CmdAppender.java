package com.sean.cmm.cli.eventsource;

import com.sean.cmm.service.CertificateService;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.java.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Log
public class CmdAppender {
    private final static String aofFile = "eventSource.aof";
    private final static File resourcesFolder = new File("src/main/resources");
    private final static Path filePath = Paths.get(resourcesFolder.getAbsolutePath(), aofFile);
    private final static String ERROR = "error";

    private static Boolean RECOVERY_ING = false;

    public static void trace(String input) {
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                log.info("AOF - Failed to create file " + filePath);
            }
        }
        append(input);
    }

    public static void error() {
        append(ERROR);
    }

    public static void recoveryDetect(CertificateService service) {
        if (!endWithError()) {
            clear();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String input = line.trim();
                if (ERROR.equals(input)) {
                    break;
                }
                if (!"".equals(input)) {
                    RECOVERY_ING = true;
                    log.info("$ " + input);
//                    InputParser.parse(line, service);
                }
            }
        } catch (IOException e) {
            log.info("Failed to read file " + filePath);
        }
        removeError();
        log.info("AOF - recovery");
    }

    private static void clear() {
        try (FileWriter fileWriter = new FileWriter(filePath.toFile(), false)) {
            fileWriter.write("");
        } catch (IOException e) {
            log.info("AOF - Failed to clear file " + filePath);
        }
    }

    private static void removeError() {
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.info("Failed to read lines from file " + filePath);
            return;
        }
        List<String> newLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.equals(ERROR)) {
                newLines.add(line);
            }
        }
        try {
            Files.write(filePath, newLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            log.info("Failed to write lines to file " + filePath);
        }
    }

    private static Boolean endWithError() {
        return Files.exists(filePath) && ERROR.equals(getLastLine());
    }

    private static String getLastLine() {
        String lastLine = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            lastLine = reader.lines().reduce((a, b) -> b).orElse(null);
        } catch (IOException e) {
            log.info("AOF - Failed to read file " + filePath);
        }
        return lastLine;
    }

    private static void append(String input) {
        if (!RECOVERY_ING) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
                writer.write("\r\n" + input);
            } catch (IOException e) {
                log.info("AOF - Failed to append content to file " + filePath);
            }
        }
    }

}
