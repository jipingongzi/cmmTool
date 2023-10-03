package xc.investigation.base.api.vo.exam;

import lombok.Data;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.ExamQuestionType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
public class QuestionAddVo {

    private Long questionTreeId;
    @NotBlank(message = "请输入问题")
    private String title;
    @NotNull(message = "问题类型必填")
    private ExamQuestionType type;
    @NotNull(message = "请设置问题分数")
    private Integer point;
    private Boolean endFlag;
    @Valid
    private List<OptionAddVo> optionAddVoList;

    public QuestionAddVo(Long questionTreeId,String title, ExamQuestionType type, Integer point, Boolean endFlag, List<OptionAddVo> optionAddVoList) {
        this.questionTreeId = questionTreeId;
        this.title = title;
        this.type = type;
        this.point = point;
        this.endFlag = endFlag;
        this.optionAddVoList = optionAddVoList;
    }
}
