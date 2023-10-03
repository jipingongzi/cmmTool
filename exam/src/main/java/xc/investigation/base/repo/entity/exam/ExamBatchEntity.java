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
@Table(name = "exam_batch")
@NoArgsConstructor
@Getter
public class ExamBatchEntity extends BaseEntity {

    private String title;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
    /**
     * is current valid
     */
    private Boolean currentFlag;

    public ExamBatchEntity(Long id, Long createId, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, createId);
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.currentFlag = false;
    }

    public void valid(Long updateId){
        this.updateTime = LocalDateTime.now();
        this.updateId = updateId;
        this.currentFlag = true;
    }
}
