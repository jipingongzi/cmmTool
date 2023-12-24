package xc.investigation.base.domain;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.domain.Model.BankModel;
import xc.investigation.base.repo.entity.bank.BankEntity;
import xc.investigation.base.repo.jpa.bank.BankJpaRepo;
import xc.investigation.base.utils.IdUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 银行领域服务
 *
 * @author ibm
 */
@Service
public class BankDomainService {

    private final BankJpaRepo bankJpaRepo;

    public BankDomainService(BankJpaRepo bankJpaRepo) {
        this.bankJpaRepo = bankJpaRepo;
    }

    private static final DecimalFormat BANK_CODE_DECIMAL = new DecimalFormat("0000");
    private static final String BANK_CODE_SPLIT = "-";

    /**
     * 创建银行
     *
     * @param adminId      管理员id
     * @param name         银行名称
     * @param description  银行描述
     * @param managerName  管理员名称
     * @param managerPhone 管理员联系方式
     * @param parentBankId 上级银行id
     * @return 银行模型
     */
    @Transactional(rollbackFor = Exception.class)
    public BankModel create(Long adminId, String name,
                            String description, String managerName, String managerPhone,
                            Long parentBankId) {

        Optional<BankEntity> parentBankOptional = bankJpaRepo.findById(parentBankId);
        if (!parentBankOptional.isPresent()) {
            throw new BizException("上级银行不存在！");
        }
        BankEntity parentBank = parentBankOptional.get();
        if (parentBank.getLeaf()) {
            throw new BizException("上级银行被限制无支行！");
        }
        String bankCode = generateBankCode(parentBank);
        Long id = IdUtil.generateId();

        BankEntity bankEntity = new BankEntity(id, name, bankCode, description, managerName, managerPhone, adminId);
        bankJpaRepo.save(bankEntity);
        return buildBankModel(bankEntity.getId());
    }

    private String generateBankCode(BankEntity parentBank) {
        List<BankEntity> subBankList = bankJpaRepo.findByCodeLikeOrderByCodeDesc(parentBank.getCode() + "-%");
        int level = StrUtil.count(parentBank.getCode(), BANK_CODE_SPLIT) + 1;
        subBankList = subBankList.stream().filter(b -> StrUtil.count(parentBank.getCode(), BANK_CODE_SPLIT) == level).collect(Collectors.toList());

        String bankCode = BANK_CODE_DECIMAL.format(1);
        if (!CollectionUtils.isEmpty(subBankList)) {
            String lastBankCode = subBankList.get(subBankList.size() - 1).getCode();
            String[] lastBankCodeItems = lastBankCode.split(BANK_CODE_SPLIT);
            int lastBankCodeNumber = Integer.parseInt(lastBankCodeItems[lastBankCodeItems.length - 1]);
            Integer bankCodeNumber = lastBankCodeNumber + 1;
            bankCode = parentBank.getCode() + BANK_CODE_SPLIT + BANK_CODE_DECIMAL.format(bankCodeNumber);
        }
        return bankCode;
    }

    private BankModel buildBankModel(Long id) {
        return null;
    }
}
