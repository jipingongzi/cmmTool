package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.ExamPaperStatus;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ibm
 */
@Entity
@Table(name = "exam_paper")
@NoArgsConstructor
@Getter
public class ExamPaperEntity extends BaseEntity {

    private Long batchId;

    private String title;

    private String description;

    private Integer point;
    /**
     * all question number
     */
    private Integer questionNumber;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (15)")
    private ExamPaperStatus status;


    public ExamPaperEntity(Long id, Long createId,Long batchId, String title, String description) {
        super(id, createId);
        this.batchId = batchId;
        this.title = title;
        this.description = description;
        this.status = ExamPaperStatus.DRAFT;
    }

    public void sum(List<ExamPaperQuestionTreeEntity> questionEntityList){
        this.point = questionEntityList.stream().mapToInt(ExamPaperQuestionTreeEntity::getPoint).sum();
        this.questionNumber = questionEntityList.size();
    }

    public void complete(Long updateId){
        this.status = ExamPaperStatus.ENABLE;
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
    }

    public void disable(Long updateId){
        this.status = ExamPaperStatus.DISABLE;
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
    }

    public void update(Long updateId,Long batchId, String title, String description){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.batchId = batchId;
        this.title = title;
        this.description = description;
    }
}
