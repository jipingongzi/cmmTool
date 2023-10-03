package xc.investigation.base.api.vo.exam;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
public class OptionAddVo {

    private Long optionTreeId;
    @NotBlank(message = "请输入选项")
    private String title;
    @NotNull(message = "请设置正确选项")
    private Boolean correctFlag;
    private Boolean endFlag;
    /**
     * @see xc.investigation.base.constant.domain.ExamQuestionType
     * 如果是UN_SETTLED_CHOICE，每个选项可以单独设置分数
     */
    private Integer point;
    /**
     * 不同的选项可能导致不同的答题路径
     */
    @Valid
    private List<QuestionAddVo> questionAddVoList;

    public OptionAddVo(Long optionTreeId,String title, Boolean correctFlag, Boolean endFlag, Integer point) {
        this.optionTreeId = optionTreeId;
        this.title = title;
        this.correctFlag = correctFlag;
        this.endFlag = endFlag;
        this.point = point;
    }
}
