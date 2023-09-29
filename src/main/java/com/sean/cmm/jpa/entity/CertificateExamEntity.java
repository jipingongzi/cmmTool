package com.sean.cmm.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 当前测试实体
 * @author seanx
 */
@Entity
@Table(name = "t_certificate_exam")
@Getter
@NoArgsConstructor
public class CertificateExamEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    /**
     * 测验名称
     */
    @Column(name = "exam_name",nullable = false)
    private String examName;

    @Column(name = "current",nullable = false)
    private Boolean current;

    @Column(name = "create_time",nullable = false)
    private LocalDateTime createTime;
    @Column(name = "update_time",nullable = false)
    private LocalDateTime updateTime;
    @Column(name = "entity_status",nullable = false)
    private String entityStatus;

}
