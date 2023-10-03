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
@Table(name = "exam_operation_log")
@NoArgsConstructor
@Getter
public class ExamOperationLogEntity extends BaseEntity {

}
