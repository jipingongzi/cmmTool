package xc.investigation.base.repo.jpa.sys;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.sys.SysAdminEntity;

import java.util.Optional;

/**
 * @author ibm
 */
public interface SysAdminJpaRepo extends JpaRepository<SysAdminEntity,Long> {

    Optional<SysAdminEntity> findByNameAndBankCode(String name,String bankCode);

    Optional<SysAdminEntity> findByNameAndPwd(String name,String pwd);
}
