package com.sean.cmm.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author seanx
 * 理论考试
 */
@Data
public class CertificateTheoryTestDto {
    private String userName;
    private String userIdNo;
    private String bankInfo;
    private String examStartTime;
    private String examSuccessTime;
    private String area;
    private BigDecimal score;

    public CertificateTheoryTestDto(String userName, String userIdNo, String bankInfo, String examStartTime, String examSuccessTime, String area, BigDecimal score) {
        this.userName = userName;
        this.userIdNo = userIdNo.trim();
        this.bankInfo = bankInfo;
        this.examStartTime = examStartTime;
        this.examSuccessTime = examSuccessTime;
        this.area = area;
        this.score = score;
    }
}
