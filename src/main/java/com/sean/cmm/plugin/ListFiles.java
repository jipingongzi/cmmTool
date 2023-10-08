package com.sean.cmm.plugin;

import java.io.File;

public class ListFiles {

    public static void main(String[] args) {
        // 指定要读取的目录，例如："/Users/yourusername/Documents"
        String directoryPath = "your_directory_path_here";

        // 调用 listFiles 方法
        listFiles(directoryPath);
    }

    public static void listFiles(String directoryPath) {
        // 创建 File 对象
        File directory = new File(directoryPath);

        // 获取目录下的所有文件和子目录
        File[] files = directory.listFiles();

        // 如果目录不为空，遍历并打印所有文件名
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                } else if (file.isDirectory()) {
                    // 对子目录递归调用 listFiles 方法
                    listFiles(file.getAbsolutePath());
                }
            }
        } else {
            System.out.println("The specified path is not a directory.");
        }
    }

    private String parseFileName(String originalFileName){
        return "";
    }
}
