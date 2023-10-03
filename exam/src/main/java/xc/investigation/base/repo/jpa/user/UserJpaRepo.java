package xc.investigation.base.repo.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import xc.investigation.base.repo.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * @author ibm
 */
public interface UserJpaRepo extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByIdNo(String idNo);

    Optional<UserEntity> findByIdNoAndPwd(String idNo,String pwd);

    List<UserEntity> findByNameLike(String name);
}
