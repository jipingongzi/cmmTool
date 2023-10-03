package xc.investigation.base.api.vo.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ibm
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperHtmlVo {
    private Long paperId;
    private String html;
}
