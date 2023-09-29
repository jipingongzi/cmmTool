package com.sean.cmm.service;

import com.sean.cmm.dto.CertificateResultDto;
import com.sean.cmm.jpa.entity.CertificateEntity;
import com.sean.cmm.jpa.entity.CertificateExamEntity;
import com.sean.cmm.jpa.jpa.CertificateExamJpaRepo;
import com.sean.cmm.jpa.jpa.CertificateJpaRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 证书业务
 * @author seanx
 */
@Service
@Slf4j
public class CertificateService {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final CertificateJpaRepo certificateJpaRepo;
    private final CertificateExamJpaRepo certificateExamJpaRepo;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificateService(CertificateJpaRepo certificateJpaRepo, CertificateExamJpaRepo certificateExamJpaRepo, JdbcTemplate jdbcTemplate) {
        this.certificateJpaRepo = certificateJpaRepo;
        this.certificateExamJpaRepo = certificateExamJpaRepo;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveCertificate(List<CertificateResultDto> certificateDtoList){
        Optional<CertificateExamEntity> examEntityOptional = certificateExamJpaRepo.findByCurrent(true);
        if(!examEntityOptional.isPresent()){
            throw new RuntimeException("当前无认证测验");
        }
        CertificateExamEntity certificateExamEntity = examEntityOptional.get();

        certificateDtoList.forEach(dto -> {
            Optional<CertificateEntity> oldCertificateEntityOptional = certificateJpaRepo.findByUserIdNoAndExamId(dto.getUserIdNo(), certificateExamEntity.getId());
            if(oldCertificateEntityOptional.isPresent()){
                CertificateEntity certificate = oldCertificateEntityOptional.get();
                certificate.update(dto.getUserName(), dto.getUserArea(), dto.getUserBankInfo(),
                        dto.getExamStartTime(), dto.getExamSuccessTime(), dto.getTotalScore());
                certificateJpaRepo.save(certificate);
                log.info(String.format("%s %s 进行更新", dto.getUserName(), dto.getUserIdNo()));
            }

            if (!oldCertificateEntityOptional.isPresent() && dto.getTotalScore().compareTo(BigDecimal.valueOf(90.0)) >= 0) {
                certificateJpaRepo.save(new CertificateEntity(generateCode(dto, certificateExamEntity.getId()),
                        dto.getUserName(), dto.getUserArea(), dto.getUserIdNo(), dto.getUserBankInfo(),
                        certificateExamEntity.getId(), certificateExamEntity.getExamName(), dto.getExamStartTime(),
                        dto.getExamSuccessTime(), dto.getTotalScore()));
            }
        });
    }

    private String generateCode(CertificateResultDto dto,int currentExamId){
        String sql = "select count(*) as flag from t_certificate where user_area = ? and exam_id = ?";
        List<Map<String,Object>> resultSet = jdbcTemplate.queryForList(sql,dto.getUserArea(),currentExamId);
        int dbLocation = Integer.parseInt(resultSet.get(0).get("flag").toString());
        int currentLocation = dbLocation + 1;

        return LocalDateTime.now().format(dateTimeFormatter) + dto.getUserArea() + String.format("%04d", currentLocation);
    }

}
