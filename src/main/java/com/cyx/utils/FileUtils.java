package com.cyx.utils;

public class FileUtils {

    public static String getFileTypeImagePath(String fileName) {
        String suffix = fileName.split("\\.")[fileName.split("\\.").length - 1];
        switch (suffix){
            case "ppt":
            case "pptx":
                return "/images/file/ppt.png";
            case "doc":
            case "docx":
                return "/images/file/word.png";
            case "xls":
            case "xlsx":
                return "/images/file/excel.png";
            case "pdf":
                return "/images/file/pdf.png";
            case "zip":
            case "rar":
                return "/images/file/zip.png";
            case "txt":
                return "/images/file/txt.png";
            default:
                return "/images/file/unknown_file.png";
        }
    }
}
