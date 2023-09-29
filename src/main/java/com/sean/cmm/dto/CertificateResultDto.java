package com.sean.cmm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 总成绩Dto
 * @author seanx
 */
@Data
@AllArgsConstructor
public class CertificateResultDto {
    private String userIdNo;
    private String userName;
    private String userBankInfo;
    private String userArea;

    private String examStartTime;
    private String examSuccessTime;

    private BigDecimal completeRateScore;
    private BigDecimal practiceTestScore;
    private BigDecimal manuallyTestScore;
    private BigDecimal theoryTestScore;

    public BigDecimal getTotalScore(){
        return completeRateScore.add(practiceTestScore).add(theoryTestScore).add(manuallyTestScore);
    }
}
