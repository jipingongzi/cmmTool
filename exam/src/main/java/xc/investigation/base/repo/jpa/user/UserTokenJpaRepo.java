package xc.investigation.base.repo.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.user.UserTokenEntity;

import java.util.Optional;

/**
 * @author ibm
 */
public interface UserTokenJpaRepo extends JpaRepository<UserTokenEntity,Long> {

    Optional<UserTokenEntity> findByToken(String token);

    Optional<UserTokenEntity> findByCreateId(Long userId);
}
