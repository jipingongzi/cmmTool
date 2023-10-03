package xc.investigation.base.repo.jpa.sys;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.sys.SysModuleEntity;

/**
 * @author ibm
 */
public interface SysModuleJpaRepo extends JpaRepository<SysModuleEntity,Long> {

}
