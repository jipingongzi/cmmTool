package com.sean.cmm.util;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WordReader {

    public static void read(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (filePath.endsWith(".doc")) {
                    readDocFile(file);
                } else if (filePath.endsWith(".docx")) {
                    readDocxFile(file);
                } else {
                    System.out.println("Invalid Word file format, only .doc and .docx are supported.");
                }
            } else {
                System.out.println("File does not exist: " + filePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取 .doc 文件
    private static void readDocFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            HWPFDocument doc = new HWPFDocument(fis);
            WordExtractor extractor = new WordExtractor(doc);

            for (String line : extractor.getParagraphText()) {
                System.out.println(line);
            }
        }
    }

    // 读取 .docx 文件
    private static void readDocxFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            XWPFDocument doc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);

            for (String line : extractor.getText().split("\n")) {
                System.out.println(line);
            }
        }
    }
}