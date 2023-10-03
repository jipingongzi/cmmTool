package xc.investigation.base.repo.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author ibm
 */
@Entity
@Table(name = "user_group_mapping")
@NoArgsConstructor
@Getter
public class UserGroupMappingEntity extends BaseEntity {

    private Long groupId;

    private Long userId;

}
