package xc.investigation.base.api.vo.exam;

import lombok.Data;
import xc.investigation.base.dto.ExamPaperDto;
import xc.investigation.base.dto.ExamPaperInstanceDto;
import xc.investigation.base.dto.ExamPaperInstanceEditableDto;

/**
 * @author ibm
 */
@Data
public class PaperInstanceVo {

    private ExamPaperInstanceEditableDto editable;

    private ExamPaperInstanceDto instanceDto;

    private PaperCreateVo paperVo;

    public PaperInstanceVo(ExamPaperInstanceEditableDto editable,ExamPaperInstanceDto paperInstanceDto,ExamPaperDto paperDto) {
        this.editable = editable;
        this.instanceDto = paperInstanceDto;
        this.paperVo = PaperCreateVo.buildVo(paperDto);
    }
}
