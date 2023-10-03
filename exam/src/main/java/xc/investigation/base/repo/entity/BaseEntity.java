package xc.investigation.base.repo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author ibm
 */
@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    protected Long id;

    protected Long createId;

    protected Long updateId;

    protected LocalDateTime createTime;

    protected LocalDateTime updateTime;

    public BaseEntity(Long id,Long createId) {
        LocalDateTime now = LocalDateTime.now();
        this.id = id;
        this.createId = createId;
        this.updateId = createId;
        this.createTime = now;
        this.updateTime = now;
    }

    public BaseEntity(Long createId) {
        LocalDateTime now = LocalDateTime.now();
        this.createId = createId;
        this.updateId = createId;
        this.createTime = now;
        this.updateTime = now;
    }
}
