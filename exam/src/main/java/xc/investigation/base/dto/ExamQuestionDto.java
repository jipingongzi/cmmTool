package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import xc.investigation.base.constant.domain.ExamQuestionType;

import java.util.List;

/**
 * @author ibm
 */
@Data
@AllArgsConstructor
public class ExamQuestionDto {

    private Long questionId;
    private Long questionTreeId;

    private String title;
    private ExamQuestionType type;

    private Integer point;
    private Boolean rootFlag;
    private Boolean endFlag;
    private String nextPaperQuestionTreeId;

    private List<ExamQuestionOptionDto> optionDtoList;




}
