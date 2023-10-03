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
@Table(name = "sys_module_function")
@Getter
@NoArgsConstructor
public class SysModuleFunctionEntity extends BaseEntity {

    private Long moduleId;

    private String title;

    private String innerPath;

}
