package xc.investigation.base.domain.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import xc.investigation.base.constant.domain.UserStatus;

/**
 * 用户模型
 * @author ibm
 */
@Data
@AllArgsConstructor
public class UserModel {
    private Long id;

    private String name;

    private String idNo;

    private String portraitPath;

    private UserStatus status;

    private String currentToken;
}
