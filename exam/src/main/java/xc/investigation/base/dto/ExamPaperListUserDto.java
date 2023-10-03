package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 问卷列表数据
 * @author ibm
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExamPaperListUserDto {

    private Long id;
    private String title;
    /**
     * 是否完成过
     */
    private List<String> hasInstanceStrList;

}
