package xc.investigation.base.repo.entity.sys;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import xc.investigation.base.repo.entity.BaseEntity;
import xc.investigation.base.utils.IdUtil;


/**
 * 用户登录令牌
 * @author ibm
 */
@Entity
@Table(name = "sys_admin_token")
@Getter
@NoArgsConstructor
public class SysAdminTokenEntity extends BaseEntity {

    private String token;

    public SysAdminTokenEntity(Long createId, String token) {
        super(IdUtil.generateId(),createId);
        this.token = token;
    }
}
