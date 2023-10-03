package xc.investigation.base.repo.entity.sys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author ibm
 */
@Entity
@Table(name = "sys_module")
@Getter
@NoArgsConstructor
public class SysModuleEntity extends BaseEntity {

    private String title;

}
