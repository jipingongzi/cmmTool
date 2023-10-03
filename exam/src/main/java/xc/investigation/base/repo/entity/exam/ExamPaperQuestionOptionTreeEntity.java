package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * only :
 * SINGLE_CHOICE,MULTIPLE_CHOICE,JUDGMENT;
 * @author ibm
 */
@Entity
@Table(name = "exam_paper_question_option_tree")
@NoArgsConstructor
@Getter
public class ExamPaperQuestionOptionTreeEntity extends BaseEntity {

    private Long questionTreeId;

    private Long optionId;

    private Long nextQuestionTreeId;

    private Boolean endFlag;

    public ExamPaperQuestionOptionTreeEntity(Long id, Long createId, Long questionTreeId, Long optionId,Boolean endFlag) {
        super(id, createId);
        this.questionTreeId = questionTreeId;
        this.optionId = optionId;
        this.endFlag = endFlag;
    }

    public void setNextQuestionTreeId(Long nextPaperQuestionTreeId) {
        this.nextQuestionTreeId = nextPaperQuestionTreeId;
    }

    public void markEnd(Long updateId){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.endFlag = true;
    }

    public void unMarkEnd(Long updateId){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.endFlag = false;
    }
}
