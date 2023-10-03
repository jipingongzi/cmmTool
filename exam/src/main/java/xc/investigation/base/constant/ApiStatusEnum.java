package xc.investigation.base.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author ibm
 */
@AllArgsConstructor
@Getter
public enum ApiStatusEnum {

    /**
     * normal
     */
    NORMAL (0),
    /**
     * api error
     */
    BIZ_EXCEPTION(-1);

    private Integer value;
}
