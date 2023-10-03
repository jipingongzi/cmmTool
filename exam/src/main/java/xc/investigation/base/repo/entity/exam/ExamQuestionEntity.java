package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.ExamQuestionType;
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
@Table(name = "exam_question")
@NoArgsConstructor
@Getter
public class ExamQuestionEntity extends BaseEntity {

    private Long paperId;

    private String title;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (25)")
    private ExamQuestionType type;

    private Integer point;



    public ExamQuestionEntity(Long id, Long createId, Long paperId, String title, ExamQuestionType type, Integer point) {
        super(id, createId);
        this.paperId = paperId;
        this.title = title;
        this.type = type;
        this.point = point;
    }

    public void update(Long updateId,String title,Integer point){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.title = title;
        this.point = point;
    }

}
