package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author ibm
 */
@Entity
@Table(name = "exam_paper_instance")
@NoArgsConstructor
@Getter
public class ExamPaperInstanceEntity extends BaseEntity {

    private Long paperId;

    private Long userId;

    private String bankCode;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (15)")
    private ExamPaperInstanceStatus status;

    private Integer point;

    /**
     * complete question number
     */
    private Integer questionNumber;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public ExamPaperInstanceEntity(Long id, Long createId, Long paperId, Long userId, String bankCode,
     LocalDateTime startTime,LocalDateTime endTime) {
        super(id, createId);
        this.paperId = paperId;
        this.userId = userId;
        this.bankCode = bankCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = ExamPaperInstanceStatus.IN_PROGRESS;
    }

    public void update(Long updateId, String bankCode, LocalDateTime startTime,LocalDateTime endTime){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.bankCode = bankCode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void complete(Long updateId,Integer point,Integer questionNumber){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();

        this.point = point;
        this.questionNumber = questionNumber;
        this.status = ExamPaperInstanceStatus.COMPLETE;
    }

    public void submit(Long updateId){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.status = ExamPaperInstanceStatus.SUBMIT;
    }

    public void setPoint(Long updateId,Integer point){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.point = point;
    }
    public void setQuestionNumber(Long updateId,Integer questionNumber){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.questionNumber = questionNumber;
    }

    public void auditSuccess(Long adminId){
        this.updateTime = LocalDateTime.now();
        this.updateId = adminId;
        this.status = ExamPaperInstanceStatus.AUDIT_SUCCESS;
    }

    public void auditFail(Long adminId){
        this.updateTime = LocalDateTime.now();
        this.updateId = adminId;
        this.status = ExamPaperInstanceStatus.AUDIT_FAIL;
    }
}
