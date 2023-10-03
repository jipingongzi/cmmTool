package xc.investigation.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;

import java.time.LocalDateTime;

/**
 * @author ibm
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class ExamMappingPaperInstanceDto {
    private Long userId;
    private Long paperId;
    private Long paperInstanceId;
    private String userName;
    private String paperTitle;
    private String userIdNo;
    private ExamPaperInstanceStatus paperInstanceStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String bankName;

}
