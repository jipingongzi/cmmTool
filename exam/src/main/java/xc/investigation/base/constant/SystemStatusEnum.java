package xc.investigation.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ibm
 */
@AllArgsConstructor
@Getter
public enum SystemStatusEnum {

    /**
     * normal
     */
    NORMAL (0),
    /**
     * system error
     */
    SYS_EXCEPTION(-1);

    private Integer value;
}
