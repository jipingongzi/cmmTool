package xc.investigation.base.repo.jpa.sys;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.sys.SysModuleFunctionEntity;

/**
 * @author ibm
 */
public interface SysModuleFunctionJpaRepo extends JpaRepository<SysModuleFunctionEntity,Long> {
}
