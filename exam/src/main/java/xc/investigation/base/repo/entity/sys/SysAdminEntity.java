package xc.investigation.base.repo.entity.sys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.constant.domain.SysAdminStatus;
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
@Table(name = "sys_admin")
@Getter
@NoArgsConstructor
public class SysAdminEntity extends BaseEntity {

    private String bankCode;

    private String name;

    private String pwd;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (15)")
    private SysAdminStatus status;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (15)")
    private SysAdminRoleType roleType;

    public SysAdminEntity(Long id, Long createId, String bankCode, String name, String pwd,SysAdminRoleType roleType) {
        super(id, createId);
        this.bankCode = bankCode;
        this.name = name;
        this.pwd = pwd;
        this.status = SysAdminStatus.ENABLE;
        this.roleType = roleType;
    }

    public void disable(Long updateId){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.status = SysAdminStatus.DISABLE;
    }

    public void enable(Long updateId){
        this.updateId = updateId;
        this.updateTime = LocalDateTime.now();
        this.status = SysAdminStatus.ENABLE;
    }
}
