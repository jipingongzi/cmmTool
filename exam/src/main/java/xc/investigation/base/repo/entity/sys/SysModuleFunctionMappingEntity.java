package xc.investigation.base.repo.entity.sys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @author ibm
 */
@Entity
@Table(name = "sys_module_function_mapping")
@Getter
@NoArgsConstructor
public class SysModuleFunctionMappingEntity extends BaseEntity {

    private Long functionId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (15)")
    private SysAdminRoleType roleType;

}
