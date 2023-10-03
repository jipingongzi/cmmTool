package xc.investigation.base.repo.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.user.UserGroupEntity;

/**
 * @author ibm
 */
public interface UserGroupJpaRepo extends JpaRepository<UserGroupEntity,Long> {

}
