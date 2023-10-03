package xc.investigation.base.dto;

import lombok.Data;
import xc.investigation.base.constant.domain.ExamPaperStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ibm
 */
@Data
public class ExamPaperDto {

    private Long id;
    private String title;
    private String description;
    private Integer point;
    private Integer questionNumber;
    private ExamPaperStatus status;

    private Long batchId;
    private String batchTitle;

    private List<ExamQuestionDto> questionDtoList;

    public ExamPaperDto(Long id, String title, String description, Integer point, Integer questionNumber, ExamPaperStatus status,Long batchId,String batchTitle) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.point = point;
        this.questionNumber = questionNumber;
        this.status = status;
        this.batchId = batchId;
        this.batchTitle = batchTitle;
        this.questionDtoList = new ArrayList<>();
    }
}
