package xc.investigation.base.domain.Model;

import cn.hutool.json.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Getter;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.constant.domain.SysAdminStatus;

/**
 * @author ibm
 */
@Getter
@AllArgsConstructor
public class SysAdminModel {
    private Long id;

    private String name;

    /**
     * 0001 0004 0010 0123
     */
    private String bankCode;
    /**
     * 中国银行 成都分行 金沙支行 蜀辉路网点
     */
    private String bankName;

    private String currentToken;

    private SysAdminStatus status;

    private SysAdminRoleType roleType;

    private JSONArray menus;

}
