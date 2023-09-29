package com.sean.cmm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 学员基本信息
 */
@Data
@AllArgsConstructor
public class StudentUserInfo {
    private String name;
    private String sex;
    private String email;
    private String phone;
    private String idCard;
    private String pwd;
    private String stuff;
    private String bank;
    private String bankLow;
    private String area;
    private String testType;
    private String serNo;
    private String content;
    private String site;
    private String group;
}
