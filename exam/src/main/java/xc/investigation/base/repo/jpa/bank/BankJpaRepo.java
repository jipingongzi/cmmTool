package xc.investigation.base.repo.jpa.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xc.investigation.base.repo.entity.bank.BankEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author ibm
 */
public interface BankJpaRepo extends JpaRepository<BankEntity,Long> {
    /**
     * 查询顶级银行
     * @return 列表
     */
    @Query(value = "FROM BankEntity WHERE root = true ")
    List<BankEntity> findRootBank();

    BankEntity findByCode(String code);

    List<BankEntity> findByCodeIn(Collection<String> codeList);
    /**
     * 根据code查询下一级
     * @param parentBankCode 父级code
     * @return
     */
    List<BankEntity> findByCodeLikeOrderByCodeDesc(String parentBankCode);
}
