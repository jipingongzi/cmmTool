package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author ibm
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ExamMappingCandidateDto {

    private String userName;
    private String idNo;
    private Long userId;

}
