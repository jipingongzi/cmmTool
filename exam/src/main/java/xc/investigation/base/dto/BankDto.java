package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankDto {
    private Long id;
    private String name;
    private String code;
    private String managerName;
    private String managerPhone;
    private Boolean root;
    private Boolean leaf;
}
