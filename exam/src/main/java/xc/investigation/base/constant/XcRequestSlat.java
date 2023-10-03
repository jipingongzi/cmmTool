package xc.investigation.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * headers
 * @author ibm
 */
@Getter
@AllArgsConstructor
public enum XcRequestSlat {
    /**
     * 前端约定固定
     */
    XC_USER_SLAT(""),
    XC_SYS_SLAT("");

    private String value;
}
