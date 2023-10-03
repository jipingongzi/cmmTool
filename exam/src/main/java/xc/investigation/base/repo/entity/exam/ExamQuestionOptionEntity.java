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
@Table(name = "exam_question_option")
@NoArgsConstructor
@Getter
public class ExamQuestionOptionEntity extends BaseEntity {

    private Long questionId;

    private String title;

    private Boolean correctFlag;

    private Boolean endFlag;
    /**
     * @see xc.investigation.base.constant.domain.ExamQuestionType
     * 如果是UN_SETTLED_CHOICE，每个选项可以单独设置分数
     */
    private Integer point;

    public ExamQuestionOptionEntity(Long id, Long createId, Long questionId, String title,Boolean endFlag, Boolean correctFlag) {
        super(id, createId);
        this.questionId = questionId;
        this.title = title;
        this.endFlag = endFlag;
        this.correctFlag = correctFlag;
    }

    public void update(Long updateId,String title){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.title = title;
    }

    public void markCorrect(Long updateId){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.correctFlag = true;
    }

    public void unMarkCorrect(Long updateId){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.correctFlag = false;
    }
    /**
     * @see xc.investigation.base.constant.domain.ExamQuestionType
     * 如果是UN_SETTLED_CHOICE，每个选项可以单独设置分数
     */
    public void setPointForUnSettledChoice(Long updateId,Integer point){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.point = point;
        this.correctFlag = true;
    }
}
