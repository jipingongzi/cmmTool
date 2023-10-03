package xc.investigation.base.dto;

import lombok.Data;

/**
*  用户注册
 * @author ibm
 */
@Data
public class UserRegisterDto {
    private String name;

    private String idNo;

    private String portraitPath;

    private String pwd;
}
