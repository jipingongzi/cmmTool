package xc.investigation.base.repo.jpa.sys;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import xc.investigation.base.repo.entity.sys.SysAdminTokenEntity;

/**
 * @author ibm
 */
public interface SysAdminTokenJpaRepo extends JpaRepository<SysAdminTokenEntity,Long> {
    Optional<SysAdminTokenEntity> findByToken(String token);

    Optional<SysAdminTokenEntity> findByCreateId(Long userId);
}
