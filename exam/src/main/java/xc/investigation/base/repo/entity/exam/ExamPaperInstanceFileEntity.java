package xc.investigation.base.repo.entity.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.ExamInstanceFileType;
import xc.investigation.base.repo.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @author ibm
 */
@Entity
@Table(name = "exam_paper_instnace_file")
@NoArgsConstructor
@Getter
public class ExamPaperInstanceFileEntity extends BaseEntity {

    private Long paperInstanceId;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR (15)")
    private ExamInstanceFileType type;

    private String url;

    private String description;

    public ExamPaperInstanceFileEntity(Long id, Long createId, Long paperInstanceId, ExamInstanceFileType type, String url, String description) {
        super(id, createId);
        this.paperInstanceId = paperInstanceId;
        this.type = type;
        this.url = url;
        this.description = description;
    }
}
