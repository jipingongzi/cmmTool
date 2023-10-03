package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.constant.domain.SysAdminStatus;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysAdminDto {
    private Long id;
    private String name;
    private String pwd;
    private String bankCode;
    private String bankName;
    private SysAdminStatus status;
    private SysAdminRoleType roleType;
}
