package xc.investigation.base.api.admin;

import org.apache.commons.langStr.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xc.investigation.base.api.vo.ResponseVo;
import xc.investigation.base.dto.BankDetailDto;
import xc.investigation.base.dto.BankDto;
import xc.investigation.base.query.BankQueryService;
import xc.investigation.base.query.ExamQueryService;
import xc.investigation.base.repo.entity.bank.BankEntity;
import xc.investigation.base.utils.WebUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ibm
 */
@RestController
@RequestMapping("/admin/bank")
public class BankAdminRest {

    private final BankQueryService bankQueryService;
    private final ExamQueryService examQueryService;
    private final WebUtil webUtil;

    public BankAdminRest(BankQueryService bankQueryService, ExamQueryService examQueryService, WebUtil webUtil) {
        this.bankQueryService = bankQueryService;
        this.examQueryService = examQueryService;
        this.webUtil = webUtil;
    }

    private static final String ADMIN_BANK_CODE = "XC_ADMIN_BANK_CODE";
    private static final String GET_BANK_LIST = "/list";
    private static final String GET_BANK_DETAIL = "/detail/{bankId}";
    private static final String GET_BANK_TOP = "/top";
    private static final String GET_BANK_ALL = "/all";

    @GetMapping(value = GET_BANK_LIST)
    public ResponseVo<Page<BankDto>> bankList(@RequestParam(value = "page",defaultValue = "1") Integer pageNo,
                                              @RequestParam(value = "rows",defaultValue = "10") Integer pageSize,
                                              @RequestParam(value = "name",required = false)String name,
                                              @RequestParam(value = "top",required = false)Boolean top,
                                              @RequestParam(value = "leaf",required = false)Boolean leaf,
                                              @RequestParam(value = "code",required = false)String code,
                                              @RequestHeader(value = ADMIN_BANK_CODE)String adminBankCode){
        String searchBankCode;
        if(!StringUtils.isNotBlank(code) || adminBankCode.length() > code.length()){
            searchBankCode = adminBankCode;
        }else {
            searchBankCode = code;
        }
        return ResponseVo.of(bankQueryService.findBankPage(pageNo - 1,pageSize,searchBankCode,name,top,leaf));
    }

    @GetMapping(value = GET_BANK_DETAIL)
    public ResponseVo<BankDetailDto> bankDetail(@PathVariable("bankId")Long bankId){
        return ResponseVo.of(new BankDetailDto(
                bankQueryService.findBankDto(bankId),
                examQueryService.findPaperInstanceListDtoByBankId(bankId)));
    }

    @GetMapping(value = GET_BANK_TOP)
    public ResponseVo<List<BankEntity>> bankTop(@RequestHeader(value = ADMIN_BANK_CODE)String code){
        return ResponseVo.of(bankQueryService.findTopBank().stream().filter(b -> b.getCode().startsWith(code)).collect(Collectors.toList()));
    }

    @GetMapping(value = GET_BANK_ALL)
    public ResponseVo<List<BankDto>> bankAll(){
        return ResponseVo.of(bankQueryService
                .findBankPage(0,Integer.MAX_VALUE,null,null,null,null).getContent());
    }
}
