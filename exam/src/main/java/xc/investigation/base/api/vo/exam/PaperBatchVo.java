package xc.investigation.base.api.vo.exam;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import xc.investigation.base.dto.ExamPaperListUserDto;

/**
 * @author ibm
 */
@Data
@AllArgsConstructor
public class PaperBatchVo {
    private Long batchId;
    private String batchTitle;
    private List<ExamPaperListUserDto> paperList;
}
