package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ibm
 */
@Data
@AllArgsConstructor
public class ExamPaperInstanceEditableDto {
    private Boolean editable;
    private String info;
}
