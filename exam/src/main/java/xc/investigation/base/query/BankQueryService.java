package xc.investigation.base.query;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xc.investigation.base.config.exception.BizException;
import xc.investigation.base.dto.BankDto;
import xc.investigation.base.repo.entity.bank.BankEntity;
import xc.investigation.base.repo.entity.bank.QBankEntity;
import xc.investigation.base.repo.jpa.bank.BankJpaRepo;
import xc.investigation.base.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ibm
 */
@Service
public class BankQueryService {

    private final BankJpaRepo bankJpaRepo;
    private final JPAQueryFactory jpaQueryFactory;

    public BankQueryService(BankJpaRepo bankJpaRepo, JPAQueryFactory jpaQueryFactory) {
        this.bankJpaRepo = bankJpaRepo;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<BankEntity> findTopBank() {
        return bankJpaRepo.findRootBank();
    }

    public BankDto findBankDto(Long bankId) {
        Optional<BankEntity> bankEntityOptional = bankJpaRepo.findById(bankId);
        if (!bankEntityOptional.isPresent()) {
            throw new BizException("银行不存在");
        }
        BankEntity bankEntity = bankEntityOptional.get();
        return new BankDto(bankEntity.getId(), bankEntity.getName(), bankEntity.getCode(),
                bankEntity.getManagerName(), bankEntity.getManagerPhone(),
                bankEntity.getRoot(), bankEntity.getLeaf());
    }

    public Page<BankDto> findBankPage(Integer pageNo, Integer pageSize, String code, String name, Boolean root, Boolean leaf) {
        QBankEntity qBankEntity = QBankEntity.bankEntity;
        List<Predicate> whereList = new ArrayList<>();
        if (StringUtils.hasText(code)) {
            whereList.add(qBankEntity.code.like(code + "%"));
        }
        if (StringUtils.hasText(name)) {
            whereList.add(qBankEntity.name.like("%" + name + "%"));
        }
        if (root != null) {
            whereList.add(qBankEntity.root.eq(root));
        }
        if (leaf != null) {
            whereList.add(qBankEntity.leaf.eq(leaf));
        }
        Predicate[] predicates = whereList.toArray(new Predicate[0]);
        JPAQuery<BankDto> bankDtoJPAQuery = jpaQueryFactory.
                select(Projections.bean(BankDto.class,
                        qBankEntity.id,
                        qBankEntity.name,
                        qBankEntity.code,
                        qBankEntity.managerName,
                        qBankEntity.managerPhone,
                        qBankEntity.root,
                        qBankEntity.leaf)).
                from(qBankEntity).
                where(predicates).
                orderBy(qBankEntity.createTime.desc()).
                offset((long) pageNo * pageSize).
                limit(pageSize);
        List<BankDto> content = bankDtoJPAQuery.fetch();
        long total = bankDtoJPAQuery.fetchCount();
        return PageUtil.pageData(content, pageNo, pageSize, total);
    }
}
