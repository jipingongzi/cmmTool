package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author ibm
 */
@Entity
@Table(name = "exam_question_instance")
@NoArgsConstructor
@Getter
public class ExamQuestionInstanceEntity extends BaseEntity {

    private Long paperInstanceId;

    private Long paperQuestionTreeId;
    /**
     * SINGLE_CHOICE:examQuestionOption.id
     * MULTIPLE_CHOICE:optionTree1.id,optionTree2.id,optionTree3.id
     * INPUT:user input text,
     * JUDGMENT:examQuestionOption.id
     */
    private String answer;

    private Integer point;

    public ExamQuestionInstanceEntity(Long id, Long createId, Long paperInstanceId, Long paperQuestionTreeId, String answer, Integer point) {
        super(id, createId);
        this.paperInstanceId = paperInstanceId;
        this.paperQuestionTreeId = paperQuestionTreeId;
        this.answer = answer;
        this.point = point;
    }

    public void point(Long updateId, Integer point){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.point = point;
    }
}
