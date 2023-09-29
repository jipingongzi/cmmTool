package com.sean.cmm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentStudyInfo {
    private String name;
    private String group;
    private String studyProcess;
    private String studyTime;
    private String studyCourse;
    private String studyTest;
}
