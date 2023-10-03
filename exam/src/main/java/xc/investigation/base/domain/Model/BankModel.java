package xc.investigation.base.domain.Model;

import lombok.Data;

import java.util.List;

/**
 * @author ibm
 */
@Data
public class BankModel {
    private Long id;

    private String name;

    private String bankCode;
    /**
     * 中国银行 成都分行 金沙支行 蜀辉路网点
     */
    private String bankFullName;

    private String description;

    private String managerName;

    private String managerPhone;
    /**
     * 子级银行
     */
    private List<BankModel> subBankModelList;
    /**
     * 直属用户
     */
    private List<UserModel> userModelList;
}
