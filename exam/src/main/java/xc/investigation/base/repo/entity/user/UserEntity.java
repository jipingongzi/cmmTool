package xc.investigation.base.repo.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.UserStatus;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ibm
 */
@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
public class UserEntity extends BaseEntity {

    private String name;

    private String idNo;

    private String portraitPath;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (15)")
    private UserStatus status;

    private String pwd;

    public UserEntity(Long id, String name, String idNo, String portraitPath,String pwd,
    Long creatorId) {
        super(id,creatorId);
        this.name = name;
        this.idNo = idNo;
        this.portraitPath = portraitPath;
        this.pwd = pwd;
        this.status = UserStatus.ENABLE;
    }
    public void enable(){
        this.updateTime = LocalDateTime.now();
        this.status = UserStatus.ENABLE;
    }
    public void disable(){
        this.updateTime = LocalDateTime.now();
        this.status = UserStatus.DISABLE;
    }

}
