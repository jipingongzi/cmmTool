package com.sean.cmm.util;

import com.sean.cmm.dto.OssMetaData;

import java.io.File;
import java.util.List;

public final class OssUtil {
    public static String upload(String bucketName, File file){
        return "";
    }
    public static String getFileUrl(String bucketName, String fileName){
        return "";
    }

    public static Byte[] downLoad(String fileUrl){
        return null;
    }

    public static List<String> getFileUrls(String bucketName){
        return null;
    }

    public static OssMetaData getMetaData(String url){
        return new OssMetaData();
    }

}
