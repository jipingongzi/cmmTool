package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author ibm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDetailDto {
    private UserDto userDto;
    private List<ExamPaperInstanceListDto> examPaperInstanceListDtoList;
}
