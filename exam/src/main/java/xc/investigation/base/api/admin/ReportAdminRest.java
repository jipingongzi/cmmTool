package xc.investigation.base.api.admin;

import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;
import xc.investigation.base.dto.ExamPaperInstanceListDto;
import xc.investigation.base.query.ExamQueryService;
import xc.investigation.base.utils.WebUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author seanx
 */
@RequestMapping("/admin/report")
@RestController
public class ReportAdminRest {

    private final WebUtil webUtil;
    private final ExamQueryService examQueryService;

    private static final String GET_PAPER_INSTANCE_LIST = "/paper/instance/list";

    public ReportAdminRest(WebUtil webUtil, ExamQueryService examQueryService) {
        this.webUtil = webUtil;
        this.examQueryService = examQueryService;
    }

    @GetMapping(value = GET_PAPER_INSTANCE_LIST)
    public ResponseVo<Page<ExamPaperInstanceListDto>> paperInstanceList(@RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                                                        @RequestParam(value = "rows",
                                                                                defaultValue = "10") Integer pageSize,
                                                                        @RequestParam(value = "bankCode",required = false)String bankCode,
                                                                        @RequestParam(value = "paperTitle",required = false)String paperTitle,
                                                                        @RequestParam(value = "userName",required =false)String userName,
                                                                        @RequestParam(value = "batchId",required = false)Long batchId,
                                                                        @RequestParam(value = "minStartTime",required = false)String minStartTime,
                                                                        @RequestParam(value = "maxStartTime",required = false)String maxStartTime,
                                                                        @RequestParam(value = "minPoint",required = false)Integer minPoint,
                                                                        @RequestParam(value = "maxPoint",required = false)Integer maxPoint){
        return ResponseVo.of(examQueryService.findPaperInstancePage(pageNo - 1,pageSize,
                batchId,null,paperTitle,userName, webUtil.checkBankCode(bankCode),
                minPoint,maxPoint,ExamPaperInstanceStatus.AUDIT_SUCCESS,
                StringUtils.hasText(minStartTime) ? LocalDateTime.parse(minStartTime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) : null,
                StringUtils.hasText(maxStartTime) ? LocalDateTime.parse(maxStartTime, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")) : null));
    }
}
