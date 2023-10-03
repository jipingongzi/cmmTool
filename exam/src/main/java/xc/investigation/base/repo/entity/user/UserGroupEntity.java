package xc.investigation.base.repo.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户分组
 * @author ibm
 */
@Entity
@Table(name = "user_group")
@Getter
@NoArgsConstructor
public class UserGroupEntity extends BaseEntity {

    private String title;

    private String description;

}
