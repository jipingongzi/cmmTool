package com.sean.cmm.jpa.entity;

import com.xc.software.common.EntityStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 证书实体
 * @author seanx
 */
@Entity
@Table(name = "t_certificate")
@Getter
@NoArgsConstructor
public class CertificateEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    /**
     * 编号
     */
    @Column(name = "code",nullable = false)
    private String code;
    /**
     * 用户名
     */
    @Column(name = "user_name",nullable = false)
    private String userName;
    /**
     * 用户区域
     */
    @Column(name = "user_area",nullable = false)
    private String userArea;
    /**
     * 用户身份证
     */
    @Column(name = "user_id_no",nullable = false)
    private String userIdNo;
    /**
     * 用户银行信息
     */
    @Column(name = "user_bank_info",nullable = false)
    private String userBankInfo;
    /**
     * 测验id
     */
    @Column(name = "exam_id",nullable = false)
    private Integer examId;
    /**
     * 测验名称
     */
    @Column(name = "exam_name",nullable = false)
    private String examName;
    /**
     * 测验开始时间
     */
    @Column(name = "exam_start_time",nullable = false)
    private String examStartTime;
    /**
     * 测验通过时间
     */
    @Column(name = "exam_success_time",nullable = false)
    private String examSuccessTime;

    @Column(name = "score",nullable = false)
    private BigDecimal score;

    @Column(name = "create_time",nullable = false)
    private LocalDateTime createTime;
    @Column(name = "update_time",nullable = false)
    private LocalDateTime updateTime;
    @Column(name = "entity_status",nullable = false)
    private String entityStatus;

    public CertificateEntity(String code,
                             String userName,String userArea,String userIdNo,String userBankInfo,
                             Integer examId,String examName,String examStartTime,String examSuccessTime,
                             BigDecimal score){
        this.code = code;
        this.userName = userName;
        this.userArea = userArea;
        this.userIdNo = userIdNo;
        this.userBankInfo = userBankInfo;

        this.examId = examId;
        this.examName = examName;
        this.examStartTime = examStartTime;
        this.examSuccessTime = examSuccessTime;

        this.score = score;

        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
        this.entityStatus = EntityStatusEnum.VALID.name();
    }

    public void update(String userName,String userArea,String userBankInfo,
                       String examStartTime,String examSuccessTime, BigDecimal score){
        this.userName = userName;
        this.userArea = userArea;
        this.userBankInfo = userBankInfo;

        this.examStartTime = examStartTime;
        this.examSuccessTime = examSuccessTime;

        this.score = score;

        LocalDateTime now = LocalDateTime.now();
        this.updateTime = now;
        this.entityStatus = EntityStatusEnum.VALID.name();
    }
}
