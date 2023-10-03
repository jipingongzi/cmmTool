package xc.investigation.base.api.vo.exam;

import lombok.Data;

import java.util.List;

/**
 * @author ibm
 */
@Data
public class UserMappingVo {
    private Long paperId;
    private List<Long> userGroupIdList;
    private List<Long> userIdList;
}
