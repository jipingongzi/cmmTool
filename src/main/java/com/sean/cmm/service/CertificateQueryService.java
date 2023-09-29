package com.sean.cmm.service;

import com.sean.cmm.jpa.entity.CertificateEntity;
import com.sean.cmm.jpa.entity.CertificateExamEntity;
import com.sean.cmm.jpa.jpa.CertificateExamJpaRepo;
import com.sean.cmm.jpa.jpa.CertificateJpaRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 证书查询
 * @author seanx
 */
@Service
public class CertificateQueryService {

    private final CertificateExamJpaRepo certificateExamJpaRepo;
    private final CertificateJpaRepo certificateJpaRepo;

    public CertificateQueryService(CertificateExamJpaRepo certificateExamJpaRepo, CertificateJpaRepo certificateJpaRepo) {
        this.certificateExamJpaRepo = certificateExamJpaRepo;
        this.certificateJpaRepo = certificateJpaRepo;
    }

    public Optional<CertificateEntity> get(int certificateId){
        return certificateJpaRepo.findById(certificateId);
    }
    public int getCertificateId(String userIdNo){
        Optional<CertificateExamEntity> examEntityOptional = certificateExamJpaRepo.findByCurrent(true);
        if(!examEntityOptional.isPresent()){
            return -1;
        }
        CertificateExamEntity certificateExamEntity = examEntityOptional.get();
        Optional<CertificateEntity> certificateEntityOptional = certificateJpaRepo.findByUserIdNoAndExamId(userIdNo,certificateExamEntity.getId());
        return certificateEntityOptional.map(CertificateEntity::getId).orElse(-1);
    }
}
