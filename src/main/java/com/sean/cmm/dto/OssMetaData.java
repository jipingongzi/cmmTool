package com.sean.cmm.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OssMetaData {
    private String url;
    private String fileName;
    private String bucketName;
    private String size;
    private Date expireAt;
}
