package com.sean.cmm.jpa.jpa;

import com.sean.cmm.jpa.entity.CertificateExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author seanx
 */
public interface CertificateExamJpaRepo extends JpaRepository<CertificateExamEntity,Integer> {

    /**
     * 获取当前有效测验
     * @param current 永远是true
     * @return 当前测验信息
     */
    Optional<CertificateExamEntity> findByCurrent(boolean current);
}
