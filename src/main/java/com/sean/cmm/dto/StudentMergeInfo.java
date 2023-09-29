package com.sean.cmm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentMergeInfo {
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

    private String studyProcess;
    private String studyTime;
    private String studyCourse;
    private String studyTest;

    public StudentMergeInfo(StudentUserInfo userInfo, StudentStudyInfo studyInfo){
        if(userInfo != null) {
            this.name = userInfo.getName();
            this.sex = userInfo.getSex();
            this.email = userInfo.getEmail();
            this.phone = userInfo.getPhone();
            this.idCard = userInfo.getIdCard();
            this.pwd = userInfo.getPwd();
            this.stuff = userInfo.getStuff();
            this.bank = userInfo.getBank();
            this.bankLow = userInfo.getBankLow();
            this.area = userInfo.getArea();
            this.testType = userInfo.getTestType();
            this.serNo = userInfo.getSerNo();
            this.content = userInfo.getContent();
            this.site = userInfo.getSite();
            this.group = userInfo.getGroup();
        }
        if(studyInfo != null) {
            this.studyProcess = studyInfo.getStudyProcess();
            this.studyTime = studyInfo.getStudyTime();
            this.studyCourse = studyInfo.getStudyCourse();
            this.studyTest = studyInfo.getStudyTest();
        }
    }
}
