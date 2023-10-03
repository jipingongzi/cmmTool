package xc.investigation.base;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;
import xc.investigation.base.dto.ExamPaperListAdminDto;
import xc.investigation.base.dto.SysAdminDto;
import xc.investigation.base.dto.UserDto;
import xc.investigation.base.query.ExamQueryService;
import xc.investigation.base.query.UserQueryService;
import xc.investigation.base.repo.entity.bank.QBankEntity;
import xc.investigation.base.repo.entity.sys.QSysAdminEntity;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BaseApplicationTests {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	@Autowired
	private ExamQueryService examQueryService;
	@Autowired
	private UserQueryService userQueryService;

	private QBankEntity qBankEntity;
	private QSysAdminEntity qSysAdminEntity;

	@PostConstruct
	private void init(){
		qBankEntity = QBankEntity.bankEntity;
		qSysAdminEntity = QSysAdminEntity.sysAdminEntity;
	}

	@Test
	public void findBankManager(){
		Long bankId = 1L;
		List<Predicate> whereList = new ArrayList<>();
		whereList.add(qBankEntity.id.eq(bankId));

		Predicate[] predicates = whereList.toArray(new Predicate[0]);

		JPAQuery<SysAdminDto> sysAdminDtoJPAQuery = jpaQueryFactory.
				select(
						Projections.bean(
								SysAdminDto.class,
								qSysAdminEntity.id,
								qSysAdminEntity.name,
								qSysAdminEntity.pwd,
								qSysAdminEntity.bankCode,
								qBankEntity.name.as("bankName")
						)
				).
				from(qSysAdminEntity).
				join(qBankEntity).on(qSysAdminEntity.bankCode.eq(qBankEntity.code)).
				where(predicates);

		List<SysAdminDto> sysAdminDtos = sysAdminDtoJPAQuery.fetch();
		sysAdminDtos.forEach(System.out::println);

	}

	@Test
	public void examPaperQuery(){
		Page<ExamPaperListAdminDto> result = examQueryService.findPaperPageForAdmin(0,10,"",null);
		if(!CollectionUtils.isEmpty(result.getContent())){
			result.getContent().forEach(System.out::println);
		}
	}

	@Test
	public void userPaperQuery(){
		Page<UserDto> result = userQueryService.findUserPage(0,10,null,null);
		if(!CollectionUtils.isEmpty(result.getContent())){
			result.getContent().forEach(System.out::println);
		}
	}

	@Test
	public void examMappingQuery(){
	}

}
