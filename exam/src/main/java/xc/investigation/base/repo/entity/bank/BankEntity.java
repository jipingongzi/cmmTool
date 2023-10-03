package xc.investigation.base.repo.entity.bank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author ibm
 */
@Entity
@Table(name = "bank")
@NoArgsConstructor
@Getter
public class BankEntity extends BaseEntity {

    private String name;

    private String code;

    private String description;

    private String managerName;

    private String managerPhone;

    private Boolean root;
    private Boolean leaf;

    public BankEntity(Long id, String name, String code,
     String description, String managerName, String managerPhone,Long creatorId) {
        super(id,creatorId);
        this.name = name;
        this.code = code;
        this.description = description;
        this.managerName = managerName;
        this.managerPhone = managerPhone;
        this.root = false;
        this.leaf = false;
    }

    public void markLeaf(){
        this.updateTime = LocalDateTime.now();
        this.leaf = true;
    }

    public void unMarkLeaf(){
        this.updateTime = LocalDateTime.now();
        this.leaf = false;
    }
}
