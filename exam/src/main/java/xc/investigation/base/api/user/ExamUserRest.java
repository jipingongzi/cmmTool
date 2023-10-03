package xc.investigation.base.api.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.api.vo.exam.*;
import xc.investigation.base.constant.domain.ExamPaperInstanceStatus;
import xc.investigation.base.domain.ExamForCommonDomainService;
import xc.investigation.base.domain.ExamForUserDomainService;
import xc.investigation.base.dto.ExamPaperDto;
import xc.investigation.base.dto.ExamPaperInstanceDto;
import xc.investigation.base.dto.ExamPaperInstanceEditableDto;
import xc.investigation.base.dto.ExamPaperInstanceListDto;
import xc.investigation.base.dto.ExamPaperListUserDto;
import xc.investigation.base.query.ExamQueryService;
import xc.investigation.base.repo.entity.exam.ExamBatchEntity;
import xc.investigation.base.utils.WebUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ibm
 */
@RestController
@RequestMapping("/user/exam")
public class ExamUserRest {
    private final ExamForCommonDomainService examForCommonDomainService;
    private final ExamForUserDomainService examForUserDomainService;
    private final ExamQueryService examQueryService;
    private final WebUtil webUtil;

    public ExamUserRest(ExamForCommonDomainService examForCommonDomainService, ExamForUserDomainService examForUserDomainService, ExamQueryService examQueryService, WebUtil webUtil) {
        this.examForCommonDomainService = examForCommonDomainService;
        this.examForUserDomainService = examForUserDomainService;
        this.examQueryService = examQueryService;
        this.webUtil = webUtil;
    }

    private static final String GET_BATCH_LIST = "/batch/list";
    private static final String GET_PAPER_LIST = "/paper/list";
    private static final String GET_PAPER = "/paper/{paperId}";
    private static final String POST_PAPER_INSTANCE = "/paper/instance";
    private static final String GET_PAPER_INSTANCE_LIST = "/paper/instance/list";
    private static final String GET_PAPER_INSTANCE = "/paper/instance/{paperInstanceId}";
    private static final String PUT_PAPER_INSTANCE = "/paper/instance";
    private static final String PUT_PAPER_INSTANCE_COMPLETE = "/paper/instance/completion/{paperInstanceId}";
    private static final String PUT_PAPER_INSTANCE_SUBMIT = "/paper/instance/submission/{paperInstanceId}";

    @GetMapping(value = GET_BATCH_LIST)
    public ResponseVo<List<PaperBatchVo>> batchList(){
        List<PaperBatchVo> batchVoList = new ArrayList<>();
        List<ExamBatchEntity> batchEntityList = examQueryService.findBatchList("",0,Integer.MAX_VALUE).getContent();
        batchEntityList.forEach(b -> {
            PaperBatchVo batchVo = new PaperBatchVo(b.getId(),b.getTitle(),
                    examQueryService.findPaperPageForUser(b.getId(),webUtil.getCurrentUser().getId()));
            batchVoList.add(batchVo);
        });
        return ResponseVo.of(batchVoList);
    }

    @GetMapping(value = GET_PAPER_LIST)
    public ResponseVo<List<ExamPaperListUserDto>> paperList(@RequestParam("batchId") Long batchId){
        return ResponseVo.of(examQueryService.findPaperPageForUser(batchId,webUtil.getCurrentUser().getId()));
    }

    @GetMapping(value = GET_PAPER)
    public ResponseVo<PaperCreateVo> paperDetail(@PathVariable("paperId")Long paperId){
        ExamPaperDto dto = examQueryService.findPaper(paperId);
        return ResponseVo.of(PaperCreateVo.buildVo(dto));
    }

    @PostMapping(value = POST_PAPER_INSTANCE)
    public ResponseVo<String> paperInstanceCreate(@RequestBody ExamPaperInstanceDto paperInstanceDto){
        examForCommonDomainService.createPaperInstance(webUtil.getCurrentUser().getId(),paperInstanceDto.getPaperId(),
                paperInstanceDto.getBankCode(),paperInstanceDto.getStartTime(),paperInstanceDto.getEndTime(),
                paperInstanceDto.getFileList(),
                paperInstanceDto.getAnswerList());
        return ResponseVo.of(null,"创建成功");
    }

    @GetMapping(value = GET_PAPER_INSTANCE_LIST)
    public ResponseVo<List<ExamPaperInstanceListDto>> paperInstanceList(@RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
                                                                        @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                                        @RequestParam(value = "status",required = false)ExamPaperInstanceStatus status){
        return ResponseVo.of(examQueryService.findPaperInstancePage(pageNo - 1,pageSize,null,
                webUtil.getCurrentUser().getId(), null,null,null,
                null,null,status,null,null)
                .getContent());
    }

    @GetMapping(value = GET_PAPER_INSTANCE)
    public ResponseVo<PaperInstanceVo> paperInstanceDetail(@PathVariable("paperInstanceId")Long paperInstanceId){
        ExamPaperInstanceDto paperInstanceDto = examQueryService.findPaperInstance(paperInstanceId);
        ExamPaperInstanceEditableDto editable = examForCommonDomainService.checkEditable(paperInstanceId,webUtil.getCurrentUser().getId());
        ExamPaperDto paperDto = examQueryService.findPaper(paperInstanceDto.getPaperId());
        return ResponseVo.of(new PaperInstanceVo(editable,paperInstanceDto,paperDto));
    }

    @PutMapping(value = PUT_PAPER_INSTANCE)
    public ResponseVo<String> paperInstanceEdit(@RequestBody ExamPaperInstanceDto paperInstanceDto){
        examForCommonDomainService.editPaperInstance(paperInstanceDto.getId(),webUtil.getCurrentUser().getId(),
                paperInstanceDto.getBankCode(),paperInstanceDto.getStartTime(),paperInstanceDto.getEndTime(),paperInstanceDto.getFileList(),paperInstanceDto.getAnswerList());
        return ResponseVo.of(null,"编辑成功");
    }

    @PutMapping(value = PUT_PAPER_INSTANCE_COMPLETE)
    public ResponseVo<String> paperInstanceComplete(@PathVariable Long paperInstanceId){
        examForUserDomainService.completePaper(webUtil.getCurrentUser().getId(),paperInstanceId);
        return ResponseVo.of(null,"问卷已完成");
    }

    @PutMapping(value = PUT_PAPER_INSTANCE_SUBMIT)
    public ResponseVo<String> paperInstanceSubmit(@PathVariable Long paperInstanceId){
        examForUserDomainService.submitPaper(webUtil.getCurrentUser().getId(),paperInstanceId);
        return ResponseVo.of(null,"问卷已提交");
    }

}
