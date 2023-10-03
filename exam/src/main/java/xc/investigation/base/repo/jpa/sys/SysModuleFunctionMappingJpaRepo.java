package xc.investigation.base.repo.jpa.sys;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.constant.domain.SysAdminRoleType;
import xc.investigation.base.repo.entity.sys.SysModuleFunctionMappingEntity;

import java.util.List;

/**
 * @author ibm
 */
public interface SysModuleFunctionMappingJpaRepo extends JpaRepository<SysModuleFunctionMappingEntity,Long> {
    List<SysModuleFunctionMappingEntity> findByRoleType(SysAdminRoleType roleType);
}
