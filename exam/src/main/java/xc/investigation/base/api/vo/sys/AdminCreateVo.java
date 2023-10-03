package xc.investigation.base.api.vo.sys;

import lombok.Data;
import xc.investigation.base.constant.domain.SysAdminRoleType;

/**
 * @author ibm
 */
@Data
public class AdminCreateVo {

    private String name;
    private String pwd;
    private String bankCode;
    private SysAdminRoleType roleType;
}
