package xc.investigation.base.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xc.investigation.base.repo.entity.exam.ExamPaperInstanceEntity;
import xc.investigation.base.repo.entity.exam.ExamQuestionInstanceEntity;
import xc.investigation.base.repo.jpa.exam.ExamBatchJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperInstanceFileJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperInstanceJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperQuestionOptionTreeJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamPaperQuestionTreeJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionInstanceFileJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionInstanceJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamQuestionOptionJpaRepo;
import xc.investigation.base.repo.jpa.exam.ExamUserMappingJpaRepo;

import java.util.List;

/**
 * @author ibm
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExamForUserDomainService {

    private final ExamBatchJpaRepo batchJpaRepo;
    private final ExamPaperJpaRepo paperJpaRepo;
    private final ExamQuestionJpaRepo questionJpaRepo;
    private final ExamQuestionOptionJpaRepo questionOptionJpaRepo;
    private final ExamPaperInstanceJpaRepo paperInstanceJpaRepo;
    private final ExamQuestionInstanceJpaRepo questionInstanceJpaRepo;
    private final ExamQuestionInstanceFileJpaRepo questionInstanceFileJpaRepo;
    private final ExamPaperInstanceFileJpaRepo paperInstanceFileJpaRepo;
    private final ExamUserMappingJpaRepo userMappingJpaRepo;
    private final ExamPaperQuestionOptionTreeJpaRepo paperQuestionOptionTreeJpaRepo;
    private final ExamPaperQuestionTreeJpaRepo paperQuestionTreeJpaRepo;
    private final ExamForCommonDomainService examForCommonDomainService;

    public ExamForUserDomainService(ExamBatchJpaRepo batchJpaRepo, ExamPaperJpaRepo paperJpaRepo, ExamQuestionJpaRepo questionJpaRepo,
                                    ExamQuestionOptionJpaRepo questionOptionJpaRepo, ExamPaperInstanceJpaRepo paperInstanceJpaRepo,
                                    ExamQuestionInstanceJpaRepo questionInstanceJpaRepo, ExamQuestionInstanceFileJpaRepo questionInstanceFileJpaRepo,
                                    ExamPaperInstanceFileJpaRepo paperInstanceFileJpaRepo, ExamUserMappingJpaRepo userMappingJpaRepo, ExamPaperQuestionTreeJpaRepo paperQuestionTreeJpaRepo, ExamPaperQuestionOptionTreeJpaRepo paperQuestionOptionTreeJpaRepo, ExamForCommonDomainService examForCommonDomainService) {
        this.batchJpaRepo = batchJpaRepo;
        this.paperJpaRepo = paperJpaRepo;
        this.questionJpaRepo = questionJpaRepo;
        this.questionOptionJpaRepo = questionOptionJpaRepo;
        this.paperInstanceJpaRepo = paperInstanceJpaRepo;
        this.questionInstanceJpaRepo = questionInstanceJpaRepo;
        this.questionInstanceFileJpaRepo = questionInstanceFileJpaRepo;
        this.paperInstanceFileJpaRepo = paperInstanceFileJpaRepo;
        this.userMappingJpaRepo = userMappingJpaRepo;
        this.paperQuestionTreeJpaRepo = paperQuestionTreeJpaRepo;
        this.paperQuestionOptionTreeJpaRepo = paperQuestionOptionTreeJpaRepo;
        this.examForCommonDomainService = examForCommonDomainService;
    }

    public void completePaper(Long userId,Long paperInstanceId){
        ExamPaperInstanceEntity paperInstanceEntity = examForCommonDomainService.findPaperInstance(paperInstanceId);
        List<ExamQuestionInstanceEntity> questionInstanceEntityList = questionInstanceJpaRepo.findByPaperInstanceId(paperInstanceId);
        paperInstanceEntity.complete(userId,
                examForCommonDomainService.calculatePaperPoint(questionInstanceEntityList),
                questionInstanceEntityList.size());
        paperInstanceJpaRepo.save(paperInstanceEntity);
    }

    public void submitPaper(Long userId,Long paperInstanceId){
        ExamPaperInstanceEntity paperInstanceEntity = examForCommonDomainService.findPaperInstance(paperInstanceId);
        paperInstanceEntity.submit(userId);
        paperInstanceJpaRepo.save(paperInstanceEntity);
    }

}
