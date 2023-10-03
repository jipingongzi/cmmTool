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
@Table(name = "exam_user_mapping")
@NoArgsConstructor
@Getter
public class ExamUserMappingEntity extends BaseEntity {

    private Long paperId;

    private Long userId;

    private Boolean special;

    public ExamUserMappingEntity(Long id, Long createId, Long paperId, Long userId) {
        super(id, createId);
        this.paperId = paperId;
        this.userId = userId;
        this.special = false;
    }
    public void markSpecial(Long adminId){
        this.updateId = adminId;
        this.updateTime = LocalDateTime.now();
        this.special = true;
    }
}
