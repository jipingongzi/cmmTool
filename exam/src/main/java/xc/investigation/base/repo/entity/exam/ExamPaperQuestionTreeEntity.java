package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author ibm
 */
@Entity
@Table(name = "exam_paper_question_tree")
@NoArgsConstructor
@Getter
public class ExamPaperQuestionTreeEntity extends BaseEntity {

    private Long paperId;

    private Long questionId;

    private Boolean rootFlag;

    private Boolean endFlag;

    private String nextPaperQuestionTreeId;

    private Integer point;

    public ExamPaperQuestionTreeEntity(Long id, Long createId, Long paperId, Long questionId, Boolean rootFlag,
     Integer point) {
        super(id, createId);
        this.paperId = paperId;
        this.questionId = questionId;
        this.rootFlag = rootFlag;
        this.endFlag = false;
        this.point = point;
    }

    public void setNext(Long nextPaperQuestionTreeId){
        if(StringUtils.hasText(this.nextPaperQuestionTreeId)){
            this.nextPaperQuestionTreeId = this.nextPaperQuestionTreeId + "," + nextPaperQuestionTreeId;
        }else {
            this.nextPaperQuestionTreeId = nextPaperQuestionTreeId + "";
        }
        this.updateTime = LocalDateTime.now();
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
