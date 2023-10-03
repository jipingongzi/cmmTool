package xc.investigation.base.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ibm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestionInstanceDto {

    private Long questionTreeId;

    private String answer;

    private List<ExamInstanceFileDto> fileList;
}
