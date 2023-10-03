package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author ibm
 */
@Entity
@Table(name = "exam_user_group_mapping")
@NoArgsConstructor
@Getter
public class ExamUserGroupMappingEntity extends BaseEntity {

    private Long paperId;

    private Long userGroupId;

    public ExamUserGroupMappingEntity(Long id, Long createId, Long paperId, Long userGroupId) {
        super(id, createId);
        this.paperId = paperId;
        this.userGroupId = userGroupId;
    }
}
