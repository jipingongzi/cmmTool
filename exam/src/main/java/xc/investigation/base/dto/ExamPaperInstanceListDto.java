package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;

import java.time.LocalDateTime;

/**
 * @author ibm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamPaperInstanceListDto {

    private Long id;

    private Long paperId;
    private String paperTitle;

    private String userName;
    private String bankName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private ExamPaperInstanceStatus status;

    private Integer point;

    /**
     * complete question number
     */
    private Integer questionNumber;


}
