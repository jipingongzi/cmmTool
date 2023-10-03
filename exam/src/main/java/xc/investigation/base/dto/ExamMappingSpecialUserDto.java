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
public class ExamMappingSpecialUserDto {

    private Long paperId;
    private Long userId;
    private String userName;
}
