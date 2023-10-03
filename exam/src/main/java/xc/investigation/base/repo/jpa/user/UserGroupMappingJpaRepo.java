package xc.investigation.base.repo.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.user.UserGroupMappingEntity;

import java.util.Collection;
import java.util.List;

/**
 * @author ibm
 */
public interface UserGroupMappingJpaRepo extends JpaRepository<UserGroupMappingEntity,Long> {

    List<UserGroupMappingEntity> findByUserIdIn(Collection<Long> userIdList);

}
