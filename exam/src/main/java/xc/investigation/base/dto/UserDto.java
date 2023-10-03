package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xc.investigation.base.constant.domain.UserStatus;

import java.util.List;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private Long id;
    private String name;
    private String idNo;
    private String pwd;
    private UserStatus status;
    private List<String> groupTitle;
    private Long paperInstanceCount;
}
