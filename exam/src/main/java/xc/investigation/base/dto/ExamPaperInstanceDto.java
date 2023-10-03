package xc.investigation.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
public class ExamPaperInstanceDto {

    private Long id;

    private Long paperId;
    private Long userId;

    private String bankCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private ExamPaperInstanceStatus status;

    private Integer point;

    /**
     * complete question number
     */
    private Integer questionNumber;

    private List<ExamQuestionInstanceDto> answerList;

    private List<ExamInstanceFileDto> fileList;

    public ExamPaperInstanceDto(Long id,Long paperId, Long userId, String bankCode,
                                LocalDateTime startTime, LocalDateTime endTime,
                                ExamPaperInstanceStatus status, Integer point, Integer questionNumber) {
        this.id = id;
        this.paperId = paperId;
        this.userId = userId;
        this.bankCode = bankCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.point = point;
        this.questionNumber = questionNumber;
        this.answerList = new ArrayList<>();
    }
}
