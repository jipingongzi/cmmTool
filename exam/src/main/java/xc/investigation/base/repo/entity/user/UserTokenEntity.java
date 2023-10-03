package xc.investigation.base.repo.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.*;

/**
 * 用户登录令牌
 * @author ibm
 */
@Entity
@Table(name = "user_token")
@Getter
@NoArgsConstructor
public class UserTokenEntity extends BaseEntity {

    private String token;

    public UserTokenEntity(Long id,Long createId, String token) {
        super(id,createId);
        this.token = token;
    }
}
