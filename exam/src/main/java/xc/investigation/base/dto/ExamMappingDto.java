package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ibm
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ExamMappingDto {

    private List<ExamMappingGroupDto> groupList;
    private List<ExamMappingSpecialUserDto> userList;

}
