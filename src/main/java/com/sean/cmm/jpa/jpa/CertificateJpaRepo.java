package com.sean.cmm.jpa.jpa;

import com.sean.cmm.jpa.entity.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author seanx
 */
public interface CertificateJpaRepo extends JpaRepository<CertificateEntity,Integer> {
    /**
     * 证书查询
     * @param userIdNo 用户身份证
     * @param examId 测验id
     * @return 证书结果
     */
    Optional<CertificateEntity> findByUserIdNoAndExamId(String userIdNo,int examId);
}
