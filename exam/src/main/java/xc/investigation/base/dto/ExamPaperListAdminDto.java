package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xc.investigation.base.constant.domain.ExamPaperStatus;

/**
 * 问卷列表数据
 * @author ibm
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExamPaperListAdminDto {

    private Long id;
    private Long batchId;
    private String title;
    private String batchTitle;
    private String description;
    private Integer point;
    private Integer questionNumber;
    private ExamPaperStatus status;

}
