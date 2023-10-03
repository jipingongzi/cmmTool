package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xc.investigation.base.constant.domain.ExamInstanceFileType;

/**
 * @author ibm
 */
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExamInstanceFileDto {

    private String url;

    private String description;

    private ExamInstanceFileType type;
}
