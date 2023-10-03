package xc.investigation.base.api.vo.exam;

import lombok.Data;
import lombok.NoArgsConstructor;
import xc.investigation.base.dto.ExamPaperDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
public class PaperCreateVo {
    /**
     * 包含id说明是更新
     * 如果已经产生问卷实例则不允许修改
     * 如果没有产生问卷实例则清除原有问题重新构造
     */
    private Long id;
    @NotNull(message = "请选择批次")
    private Long batchId;
    private String batchTitle;
    @NotBlank(message = "请输入标题")
    private String title;
    private String description;
    @Valid
    private List<QuestionAddVo> questionAddVoList;
    private Integer point;
    private Integer questionNumber;

    public PaperCreateVo(Long id, Long batchId, String batchTitle, String title, String description, Integer point, Integer questionNumber) {
        this.id = id;
        this.batchId = batchId;
        this.batchTitle = batchTitle;
        this.title = title;
        this.description = description;
        this.point = point;
        this.questionNumber = questionNumber;
    }

    public static PaperCreateVo buildVo(ExamPaperDto dto){
        PaperCreateVo vo = new PaperCreateVo(dto.getId(),dto.getBatchId(),dto.getTitle(),dto.getTitle(), dto.getDescription(),
                dto.getPoint(),dto.getQuestionNumber());

        List<QuestionAddVo> questionAddVoList = new ArrayList<>();
        dto.getQuestionDtoList().forEach(questionDto -> {
            List<OptionAddVo> optionAddVoList = new ArrayList<>();
            questionDto.getOptionDtoList().forEach(optionDto -> optionAddVoList.add(
                    new OptionAddVo(optionDto.getOptionTreeId(),optionDto.getTitle(),optionDto.getCorrectFlag(),
                            optionDto.getEndFlag(),optionDto.getPoint())));
            questionAddVoList.add(new QuestionAddVo(questionDto.getQuestionTreeId(),questionDto.getTitle(),
                    questionDto.getType(),questionDto.getPoint(), questionDto.getEndFlag(),optionAddVoList));
        });
        vo.setQuestionAddVoList(questionAddVoList);
        return vo;
    }
}
