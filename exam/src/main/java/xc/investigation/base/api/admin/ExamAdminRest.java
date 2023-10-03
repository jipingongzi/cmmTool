package xc.investigation.base.api.admin;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.api.vo.exam.PaperCreateVo;
import xc.investigation.base.api.vo.exam.PaperInstanceVo;
import xc.investigation.base.api.vo.exam.UserMappingVo;
import xc.investigation.base.config.aop.AdminAuth;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;
import xc.investigation.base.domain.ExamForAdminDomainService;
import xc.investigation.base.domain.ExamForCommonDomainService;
import xc.investigation.base.dto.ExamMappingCandidateDto;
import xc.investigation.base.dto.ExamMappingDto;
import xc.investigation.base.dto.ExamMappingGroupDto;
import xc.investigation.base.dto.ExamMappingPaperInstanceDto;
import xc.investigation.base.dto.ExamMappingSpecialUserDto;
import xc.investigation.base.dto.ExamPaperAnswerDto;
import xc.investigation.base.dto.ExamPaperDto;
import xc.investigation.base.dto.ExamPaperInstanceDto;
import xc.investigation.base.dto.ExamPaperInstanceEditableDto;
import xc.investigation.base.dto.ExamPaperInstanceListDto;
import xc.investigation.base.dto.ExamPaperListAdminDto;
import xc.investigation.base.dto.ExamQuestionDto;
import xc.investigation.base.query.ExamQueryService;
import xc.investigation.base.query.SysQueryService;
import xc.investigation.base.query.UserQueryService;
import xc.investigation.base.repo.entity.exam.ExamBatchEntity;
import xc.investigation.base.repo.entity.sys.SysAdminEntity;
import xc.investigation.base.utils.ExcelUtil;
import xc.investigation.base.utils.WebUtil;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ibm
 */
@RestController
@RequestMapping("/admin/exam")
public class ExamAdminRest {

    private final ExamForCommonDomainService examForCommonDomainService;
    private final ExamForAdminDomainService examForAdminDomainService;
    private final ExamQueryService examQueryService;
    private final SysQueryService sysQueryService;
    private final UserQueryService userQueryService;
    private final WebUtil webUtil;

    public ExamAdminRest(ExamForCommonDomainService examForCommonDomainService,
                         ExamForAdminDomainService examForAdminDomainService,
                         ExamQueryService examQueryService, SysQueryService sysQueryService, UserQueryService userQueryService, WebUtil webUtil) {
        this.examForCommonDomainService = examForCommonDomainService;
        this.examForAdminDomainService = examForAdminDomainService;
        this.examQueryService = examQueryService;
        this.sysQueryService = sysQueryService;
        this.userQueryService = userQueryService;
        this.webUtil = webUtil;
    }

    private static final String GET_BATCH_LIST = "/batch/list";

    private static final String POST_PAPER = "/paper";
    private static final String GET_PAPER = "/paper/{paperId}";
    private static final String DELETE_PAPER = "/paper/{paperId}";
    private static final String PUT_PAPER_COMPLETE = "/paper/completion/{paperId}";
    private static final String PUT_PAPER_DISABLE = "/paper/disable/{paperId}";
    private static final String GET_PAPER_LIST = "/paper/list";
    private static final String GET_PAPER_EXPORT = "/paper/{paperId}/export";

    private static final String POST_PAPER_MAPPING_USER = "/paper/mapping";
    private static final String POST_PAPER_UN_MAPPING_USER = "/paper/unMapping";
    private static final String GET_PAPER_MAPPING_INFO = "/paper/mapping/info/{paperId}";
    private static final String GET_PAPER_MAPPING_CANDIDATE = "/paper/mapping/{paperId}/candidate";

    private static final String GET_PAPER_INSTANCE_LIST = "/paper/instance/list";
    private static final String GET_PAPER_INSTANCE = "/paper/instance/{paperInstanceId}";
    private static final String PUT_PAPER_INSTANCE = "/paper/instance";
    private static final String PUT_PAPER_INSTANCE_AUDIT_SUCCESS = "/paper/instance/{paperInstanceId}/audit/success";
    private static final String PUT_PAPER_INSTANCE_AUDIT_FAIL = "/paper/instance/{paperInstanceId}/audit/fail";


    @GetMapping(value = GET_BATCH_LIST)
    public ResponseVo<Page<ExamBatchEntity>> batchList(@RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                                       @RequestParam(value = "rows",defaultValue = "10") Integer pageSize,
                                                       @RequestParam(value = "title",required = false) String title){
        return ResponseVo.of(examQueryService.findBatchList(title,pageNo - 1,pageSize));
    }

    @PostMapping(value = POST_PAPER)
    public ResponseVo<Long> paperCreate(@RequestBody @Valid PaperCreateVo paperCreateVo){
        SysAdminEntity adminEntity = webUtil.getCurrentAdmin();
        Long paperId = examForAdminDomainService.savePaper(adminEntity.getId(), paperCreateVo);
        if(paperCreateVo.getId() == null) {
          return ResponseVo.of(paperId, "创建成功");
        }else {
            return ResponseVo.of(paperCreateVo.getId(), "更新成功");
        }
    }

    @PutMapping(value = PUT_PAPER_COMPLETE)
    public ResponseVo<String> paperComplete(@PathVariable("paperId")Long paperId){
        SysAdminEntity adminEntity = webUtil.getCurrentAdmin();
        examForAdminDomainService.completePaper(adminEntity.getId(),paperId);
        return ResponseVo.of(null,"问卷已启用");
    }

    @PutMapping(value = PUT_PAPER_DISABLE)
    public ResponseVo<String> paperDisable(@PathVariable("paperId")Long paperId){
        SysAdminEntity adminEntity = webUtil.getCurrentAdmin();
        examForAdminDomainService.disablePaper(adminEntity.getId(),paperId);
        return ResponseVo.of(null,"问卷已禁用");
    }

    @DeleteMapping(value = DELETE_PAPER)
    public ResponseVo<String> paperDelete(@PathVariable("paperId")Long paperId){
        SysAdminEntity adminEntity = webUtil.getCurrentAdmin();
        examForAdminDomainService.deletePaper(adminEntity.getId(),paperId);
        return ResponseVo.of(null,"问卷已删除");
    }

    @GetMapping(value = GET_PAPER)
    public ResponseVo<PaperCreateVo> paperDetail(@PathVariable("paperId")Long paperId){
        ExamPaperDto dto = examQueryService.findPaper(paperId);
        return ResponseVo.of(PaperCreateVo.buildVo(dto));
    }

    @GetMapping(value = GET_PAPER_INSTANCE)
    public ResponseVo<PaperInstanceVo> paperInstanceDetail(@PathVariable("paperInstanceId")Long paperInstanceId){
        ExamPaperInstanceDto paperInstanceDto = examQueryService.findPaperInstance(paperInstanceId);
        ExamPaperInstanceEditableDto editable = examForCommonDomainService.checkEditable(paperInstanceId,webUtil.getCurrentAdmin().getId());
        ExamPaperDto dto = examQueryService.findPaper(paperInstanceDto.getPaperId());
        return ResponseVo.of(new PaperInstanceVo(editable,paperInstanceDto,dto));
    }

    @PutMapping(value = PUT_PAPER_INSTANCE)
    public ResponseVo<String> paperInstanceEdit(@RequestBody ExamPaperInstanceDto paperInstanceDto){
        examForCommonDomainService.editPaperInstance(paperInstanceDto.getId(),webUtil.getCurrentAdmin().getId(),
                paperInstanceDto.getBankCode(),paperInstanceDto.getStartTime(),paperInstanceDto.getEndTime(),paperInstanceDto.getFileList(),paperInstanceDto.getAnswerList());
        return ResponseVo.of(null,"编辑成功");
    }

    @AdminAuth
    @PutMapping(value = PUT_PAPER_INSTANCE_AUDIT_SUCCESS)
    public ResponseVo<String> paperInstanceAuditSuccess(@PathVariable("paperInstanceId")Long paperInstanceId){
        examForAdminDomainService.auditSuccess(webUtil.getCurrentAdmin().getId(), paperInstanceId);
        return ResponseVo.of(null,"操作成功");
    }

    @AdminAuth
    @PutMapping(value = PUT_PAPER_INSTANCE_AUDIT_FAIL)
    public ResponseVo<String> paperInstanceAuditFail(@PathVariable("paperInstanceId")Long paperInstanceId){
        examForAdminDomainService.auditFail(webUtil.getCurrentAdmin().getId(), paperInstanceId);
        return ResponseVo.of(null,"操作成功");
    }

    @AdminAuth
    @GetMapping(value = GET_PAPER_LIST)
    public ResponseVo<Page<ExamPaperListAdminDto>> paperList(@RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                                             @RequestParam(value = "rows",defaultValue = "10") Integer pageSize,
                                                             @RequestParam(value = "batchId",required = false) Long batchId,
                                                             @RequestParam(value = "title",required = false) String title){
        return ResponseVo.of(examQueryService.findPaperPageForAdmin(pageNo - 1,pageSize,title,batchId));
    }

    @GetMapping(value = GET_PAPER_INSTANCE_LIST)
    public ResponseVo<Page<ExamPaperInstanceListDto>> paperInstanceList(@RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                                                        @RequestParam(value = "rows",
                                                                        defaultValue = "10") Integer pageSize,
                                                                        @RequestParam(value = "bankCode",required = false)String bankCode,
                                                                        @RequestParam(value = "paperTitle",required = false)String paperTitle,
                                                                        @RequestParam(value = "userName",required =false)String userName,
                                                                        @RequestParam(value = "status",required =false) ExamPaperInstanceStatus status,
                                                                        @RequestParam(value = "batchId",required = false)Long batchId,
                                                                        @RequestParam(value = "minPoint",required = false)Integer minPoint,
                                                                        @RequestParam(value = "maxPoint",required = false)Integer maxPoint){
        return ResponseVo.of(examQueryService.findPaperInstancePage(pageNo - 1,pageSize,
                batchId,null,paperTitle,userName, webUtil.checkBankCode(bankCode),
                minPoint,maxPoint,status,null,null));
    }

    @PostMapping(value = POST_PAPER_MAPPING_USER)
    public ResponseVo<String> paperMappingUser(@RequestBody UserMappingVo userMappingVo){
        int mappingSize = examForAdminDomainService.mappingPaperUser(webUtil.getCurrentAdmin().getId(),
                userMappingVo.getPaperId(), userMappingVo.getUserIdList(),userMappingVo.getUserGroupIdList());

        String info = String.format("问卷编号：%s，已分配给：%d 个用户",userMappingVo.getPaperId(),mappingSize);
        return ResponseVo.of(null,info);
    }

    @PostMapping(value = POST_PAPER_UN_MAPPING_USER)
    public ResponseVo<String> paperUnMappingUser(@RequestBody UserMappingVo userMappingVo){
        int unMappingSize = examForAdminDomainService.unMappingPaperUser(webUtil.getCurrentAdmin().getId(),
                userMappingVo.getPaperId(), userMappingVo.getUserIdList(),userMappingVo.getUserGroupIdList());

        String info = String.format("问卷编号：%s，已取消分配给：%d 个用户",userMappingVo.getPaperId(),unMappingSize);
        return ResponseVo.of(null,info);
    }

    @GetMapping(value = GET_PAPER_MAPPING_INFO)
    public ResponseVo<ExamMappingDto> paperMapping(@PathVariable("paperId")Long paperId){
        List<ExamMappingSpecialUserDto> userDtoList = examQueryService.getSpecialUserMapping(paperId);
        List<ExamMappingGroupDto> groupDtoList = examQueryService.getGroupMapping(paperId);
        return ResponseVo.of(new ExamMappingDto(groupDtoList,userDtoList));
    }

    @GetMapping(value = GET_PAPER_MAPPING_CANDIDATE)
    public ResponseVo<List<ExamMappingCandidateDto>> paperMappingUserQuery(@PathVariable("paperId")Long paperId,
                                                                     @RequestParam("userName")String userName){
        return ResponseVo.of(examQueryService.getCandidateForMapping(paperId,userName));
    }

    @PostMapping(value = GET_PAPER_EXPORT)
    public void paperExport(@PathVariable("paperId")Long paperId,
                            HttpServletResponse response) throws IOException {
        ExamPaperDto paperDto = examQueryService.findPaper(paperId);
        List<String> headers = new ArrayList<>(Arrays.asList("提交者", "调查银行", "开始时间", "结束时间"));
        for (ExamQuestionDto examQuestionDto : paperDto.getQuestionDtoList()) {
            headers.add(examQuestionDto.getTitle());
        }
        List<ExamMappingPaperInstanceDto> paperInstanceDtoList = examQueryService.getPaperInstanceMapping(paperId);
        List<Long> paperInstanceIdList = paperInstanceDtoList.stream().map(ExamMappingPaperInstanceDto::getPaperInstanceId).collect(Collectors.toList());
        Map<Long,List<ExamPaperAnswerDto>> questionAnswerMap = examQueryService.getAnswer(paperId,paperInstanceIdList);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("提交汇总");
        HSSFCellStyle headerStyle = ExcelUtil.headerCellStyle(workbook);
        HSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            HSSFCell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(header);
            headerCell.setCellStyle(headerStyle);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < paperInstanceDtoList.size(); i++) {
            ExamMappingPaperInstanceDto paperInstanceDto = paperInstanceDtoList.get(i);
            HSSFRow contentRow = sheet.createRow(i + 1);
            HSSFCell cell1 = contentRow.createCell(0);
            cell1.setCellValue(paperInstanceDto.getUserName());
            HSSFCell cell2 = contentRow.createCell(1);
            cell2.setCellValue(paperInstanceDto.getBankName());
            HSSFCell cell3 = contentRow.createCell(2);
            if(paperInstanceDto.getStartTime() != null){
                cell3.setCellValue(paperInstanceDto.getStartTime().format(dtf));
            }else {
                cell3.setCellValue("");
            }
            HSSFCell cell4 = contentRow.createCell(3);
            if(paperInstanceDto.getEndTime() != null) {
                cell4.setCellValue(paperInstanceDto.getEndTime().format(dtf));
            }else {
                cell4.setCellValue("");
            }
            List<ExamPaperAnswerDto> answers = questionAnswerMap.get(paperInstanceDto.getPaperInstanceId());
            if(CollectionUtils.isNotEmpty(answers)) {
                for (int j = 0; j < answers.size(); j++) {
                    HSSFCell cell = contentRow.createCell(4 + j);
                    cell.setCellValue(answers.get(j).getAnswerText());
                }
            }
        }
        ExcelUtil.excelResponse(workbook,response,"《" + paperDto.getTitle() + "》提交汇总");
    }
}
