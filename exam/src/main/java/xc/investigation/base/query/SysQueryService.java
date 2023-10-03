package xc.investigation.base.query;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xc.investigation.base.dto.SysAdminDto;
import xc.investigation.base.repo.entity.bank.QBankEntity;
import xc.investigation.base.repo.entity.sys.QSysAdminEntity;
import xc.investigation.base.repo.jpa.sys.SysAdminJpaRepo;
import xc.investigation.base.repo.jpa.sys.SysAdminTokenJpaRepo;
import xc.investigation.base.utils.PageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ibm
 */
@Service
public class SysQueryService {

    private final SysAdminJpaRepo adminJpaRepo;
    private final SysAdminTokenJpaRepo adminTokenJpaRepo;
    private final JPAQueryFactory jpaQueryFactory;

    public SysQueryService(SysAdminJpaRepo adminJpaRepo, SysAdminTokenJpaRepo adminTokenJpaRepo, JPAQueryFactory jpaQueryFactory) {
        this.adminJpaRepo = adminJpaRepo;
        this.adminTokenJpaRepo = adminTokenJpaRepo;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<SysAdminDto> findAdminPage(Integer pageNo,Integer pageSize,String bankCode,String name){
        QBankEntity qBankEntity = QBankEntity.bankEntity;
        QSysAdminEntity qSysAdminEntity = QSysAdminEntity.sysAdminEntity;

        List<Predicate> whereList = new ArrayList<>();
        if(StringUtils.hasText(bankCode)) {
            whereList.add(qSysAdminEntity.bankCode.like(bankCode + "%"));
        }
        if(StringUtils.hasText(name)) {
            whereList.add(qSysAdminEntity.name.like("%" + name + "%"));
        }
        Predicate[] predicates = whereList.toArray(new Predicate[0]);

        JPAQuery<SysAdminDto> sysAdminDtoJPAQuery = jpaQueryFactory.
                select(Projections.bean(SysAdminDto.class,
                                qSysAdminEntity.id,
                                qSysAdminEntity.name,
                                qSysAdminEntity.pwd,
                                qSysAdminEntity.bankCode,
                                qBankEntity.name.as("bankName"),
                                qSysAdminEntity.status,
                                qSysAdminEntity.roleType)
                )
                .from(qSysAdminEntity)
                .join(qBankEntity).on(qSysAdminEntity.bankCode.eq(qBankEntity.code))
                .where(predicates)
                .orderBy(qSysAdminEntity.bankCode.asc())
                .offset((long) pageNo * pageSize)
                .limit(pageSize);
        List<SysAdminDto> content = sysAdminDtoJPAQuery.fetch();
        Long total = sysAdminDtoJPAQuery.fetchCount();
        return PageUtil.pageData(content,pageNo,pageSize,total);
    }
}
