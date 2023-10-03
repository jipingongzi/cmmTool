package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ibm
 */
@Data
@AllArgsConstructor
public class ExamQuestionOptionDto {
    private Long optionId;
    private Long optionTreeId;

    private String title;
    private Boolean correctFlag;
    private Boolean endFlag;
    private Integer point;

    private Long nextQuestionTreeId;
}
