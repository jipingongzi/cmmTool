package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author seanx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamPaperAnswerDto {
    /**
     * questionInstanceId
     */
    private Long id;
    private String questionText;
    private String answer;
    private String answerText;
}
